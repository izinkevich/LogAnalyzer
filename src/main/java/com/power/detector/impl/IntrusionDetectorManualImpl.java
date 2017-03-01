package com.power.detector.impl;

import java.util.Iterator;
import java.util.Map;

import com.power.constants.IntrusionConstants;
import com.power.data.RecentLoginEntryContainer;
import com.power.detector.IntrusionDetectorManual;

public class IntrusionDetectorManualImpl implements IntrusionDetectorManual {

	public void clean(Map<String, RecentLoginEntryContainer> storage, long intrusionTimer) {
		for (Iterator<Map.Entry<String, RecentLoginEntryContainer>> it = storage.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String, RecentLoginEntryContainer> entry = it.next();
			entry.getValue().deleteOlderThan(intrusionTimer - IntrusionConstants.TIME_TO_STORE_MILLIS);
			if (entry.getValue().size() == 0) {
				it.remove();
			}
		}
	}

}
