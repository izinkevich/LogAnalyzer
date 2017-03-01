package com.power.analyzer.impl;

import java.util.HashMap;
import java.util.Map;

import com.power.analyzer.LogAnalyzer;
import com.power.data.LogEntry;
import com.power.data.RecentLoginEntryContainer;
import com.power.detector.IntrusionDetector;
import com.power.detector.IntrusionDetectorManual;
import com.power.factory.IntrusionDetectorFactory;

public class LogAnalyzerImpl implements LogAnalyzer {

	private IntrusionDetector intDetector;
	private IntrusionDetectorManual intDetectorManual;
	private Map<String, RecentLoginEntryContainer> storage = new HashMap<String, RecentLoginEntryContainer>();

	public LogAnalyzerImpl(IntrusionDetectorFactory factory) {
		intDetector = factory.createIntrusionDetector();
		intDetectorManual = factory.createIntrusionDetectorManual();
	}

	public String parseLine(String line) {
		String[] parts = line.split(",");
		if ("FAILURE".equals(parts[2])) {
			LogEntry entry = new LogEntry();
			entry.setIpAddr(parts[0]);
			entry.setTimeOfAccess(Long.parseLong(parts[1]));
			entry.setName(parts[3]);

			String ipAddr = entry.getIpAddr();
			if (storage.containsKey(ipAddr)) {
				storage.get(ipAddr).pushInto(entry.getTimeOfAccess());
			} else {
				storage.put(ipAddr, new RecentLoginEntryContainer(entry.getTimeOfAccess()));
			}
			
			long intrusionTimer = entry.getTimeOfAccess();
			intDetectorManual.clean(storage, intrusionTimer);
			if (intDetector.find(storage)) {
				return parts[0];
			}
		}
		return null;
	}

}
