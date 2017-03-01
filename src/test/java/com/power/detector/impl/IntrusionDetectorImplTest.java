package com.power.detector.impl;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.power.constants.IntrusionConstants;
import com.power.data.RecentLoginEntryContainer;
import com.power.detector.IntrusionDetector;

public class IntrusionDetectorImplTest {

	IntrusionDetector detector = new IntrusionDetectorImpl();
	
	Map<String, RecentLoginEntryContainer> storage;
	
	long intrusionTimer;

	@Before
	public void beforeTest() {
		storage = new HashMap<String, RecentLoginEntryContainer>();
		intrusionTimer = System.currentTimeMillis();
	}

	@Test
	public void testFind() {
		RecentLoginEntryContainer container1 = new RecentLoginEntryContainer();
		for (int i = 0; i < IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED; i++) {
			container1.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + i*10);
		}
		RecentLoginEntryContainer container2 = new RecentLoginEntryContainer();
		RecentLoginEntryContainer container3 = new RecentLoginEntryContainer(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + 100);
		storage.put("1.2.3.4", container1);
		storage.put("11.22.33.44", container2);
		storage.put("111.222.233.244", container3);
		
		Assert.assertTrue(detector.find(storage));
	}

	@Test
	public void testFindNot() {
		RecentLoginEntryContainer container1 = new RecentLoginEntryContainer();
		for (int i = 0; i < IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED - 2; i++) {
			container1.pushInto(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + i*10);
		}
		RecentLoginEntryContainer container2 = new RecentLoginEntryContainer();
		RecentLoginEntryContainer container3 = new RecentLoginEntryContainer(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS + 100);
		storage.put("1.2.3.4", container1);
		storage.put("11.22.33.44", container2);
		storage.put("111.222.233.244", container3);
		
		Assert.assertFalse(detector.find(storage));
	}

}
