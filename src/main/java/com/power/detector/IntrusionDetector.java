package com.power.detector;

import java.util.Map;

import com.power.data.RecentLoginEntryContainer;

public interface IntrusionDetector {

	boolean find(Map<String, RecentLoginEntryContainer> storage);

}
