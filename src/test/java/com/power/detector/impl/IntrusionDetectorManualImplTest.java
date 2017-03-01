package com.power.detector.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.power.constants.IntrusionConstants;
import com.power.data.RecentLoginEntryContainer;
import com.power.detector.IntrusionDetectorManual;

public class IntrusionDetectorManualImplTest {

	IntrusionDetectorManual detectorManual = new IntrusionDetectorManualImpl();
	long intrusionTimer = System.currentTimeMillis();
	
	Map<String, RecentLoginEntryContainer> storage;

	@Before
	public void beforeTest() {
		storage = new HashMap<String, RecentLoginEntryContainer>();
	}

	@Test
	public void testClean() {
		RecentLoginEntryContainer container1 = new RecentLoginEntryContainer(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS - 100);
		container1.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS - 90);
		container1.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS - 80);
		RecentLoginEntryContainer container2 = new RecentLoginEntryContainer(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + 100);
		container2.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + 120);
		container2.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + 140);
		RecentLoginEntryContainer container3 = new RecentLoginEntryContainer(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS - 100);
		container3.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS - 100);
		container3.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + 100);
		storage.put("1.2.3.4", container1);
		storage.put("11.22.33.44", container2);
		storage.put("111.222.233.244", container3);

		detectorManual.clean(storage, intrusionTimer);

		Assert.assertEquals(2, storage.size());
		Assert.assertTrue(!storage.containsValue(container1));
		Assert.assertTrue(storage.containsValue(container2));
		Assert.assertTrue(storage.containsValue(container3));
	}
}
