package com.power.detector;

import java.util.Map;

import com.power.data.RecentLoginEntryContainer;

public interface IntrusionDetectorManual {

	void clean(Map<String, RecentLoginEntryContainer> storage, long intrusionTimer);

}
