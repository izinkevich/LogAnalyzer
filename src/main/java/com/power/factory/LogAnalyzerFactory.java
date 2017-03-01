package com.power.factory;

import com.power.analyzer.LogAnalyzer;
import com.power.analyzer.impl.LogAnalyzerImpl;

public class LogAnalyzerFactory {

	private static LogAnalyzer analyzer = null;

	public static LogAnalyzer getInstance() {
		if (analyzer == null) {
			analyzer = new LogAnalyzerImpl(new IntrusionDetectorFactory());
		}
		return analyzer;
	}
}
