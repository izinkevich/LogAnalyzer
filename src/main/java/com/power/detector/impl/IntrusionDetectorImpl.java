package com.power.detector.impl;

import java.util.Map;

import com.power.constants.IntrusionConstants;
import com.power.data.RecentLoginEntryContainer;
import com.power.detector.IntrusionDetector;

public class IntrusionDetectorImpl implements IntrusionDetector {

	public boolean find(Map<String, RecentLoginEntryContainer> storage) {
		for(Map.Entry<String, RecentLoginEntryContainer> entry : storage.entrySet()) {
			if (entry.getValue().size() == IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED) {
				entry.getValue().deleteOneOldestToNotRepeatAlarm();
				return true;
			}
		}
		return false;
	}

}
