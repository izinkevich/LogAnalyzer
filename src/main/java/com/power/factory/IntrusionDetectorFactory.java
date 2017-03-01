package com.power.factory;

import com.power.detector.IntrusionDetector;
import com.power.detector.IntrusionDetectorManual;
import com.power.detector.impl.IntrusionDetectorImpl;
import com.power.detector.impl.IntrusionDetectorManualImpl;

public class IntrusionDetectorFactory {

	public IntrusionDetector createIntrusionDetector() {
		return new IntrusionDetectorImpl();
	}

	public IntrusionDetectorManual createIntrusionDetectorManual() {
		return new IntrusionDetectorManualImpl();
	}
}
