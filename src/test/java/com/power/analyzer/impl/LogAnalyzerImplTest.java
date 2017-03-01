package com.power.analyzer.impl;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;

import com.power.analyzer.LogAnalyzer;
import com.power.data.RecentLoginEntryContainer;
import com.power.detector.IntrusionDetector;
import com.power.detector.IntrusionDetectorManual;
import com.power.detector.impl.IntrusionDetectorImpl;
import com.power.detector.impl.IntrusionDetectorManualImpl;
import com.power.factory.LogAnalyzerFactory;

public class LogAnalyzerImplTest {
	
	IntrusionDetector intrusionDetectorMock;
    IntrusionDetectorManual intrusionDetectorManualMock;

    LogAnalyzer analyzer = LogAnalyzerFactory.getInstance();
    
    String failureline = "30.212.19.124,1462459026,FAILURE,Thomas.Davenport";
    String successline = "30.212.127.12,1462458822,SUCCESS,Peter.Rascal";

	@Before
	public void setUp() {
		intrusionDetectorMock = Mockito.mock(IntrusionDetectorImpl.class);
		intrusionDetectorManualMock = Mockito.mock(IntrusionDetectorManualImpl.class);
		Whitebox.setInternalState(analyzer, "intDetector", intrusionDetectorMock);
		Whitebox.setInternalState(analyzer, "intDetectorManual", intrusionDetectorManualMock);
	}

	@Test
	public void testParseLineIntrusion() {
		Mockito.when(intrusionDetectorMock.find(Matchers.<Map<String, RecentLoginEntryContainer>> any())).thenReturn(true);

		String result = analyzer.parseLine(failureline);

		long intrusionTimer = Long.parseLong(failureline.split(",")[1]);
		Mockito.verify(intrusionDetectorManualMock, Mockito.times(1)).clean(Matchers.<Map<String, RecentLoginEntryContainer>> any(), Matchers.eq(intrusionTimer));
		Mockito.verify(intrusionDetectorMock, Mockito.times(1)).find(Matchers.<Map<String, RecentLoginEntryContainer>> any());
		Assert.assertEquals(failureline.split(",")[0], result);
	}

	@Test
	public void testParseLineNoIntrusion() {
		Mockito.when(intrusionDetectorMock.find(Matchers.<Map<String, RecentLoginEntryContainer>> any())).thenReturn(false);

		String result = analyzer.parseLine(failureline);

		long intrusionTimer = Long.parseLong(failureline.split(",")[1]);
		Mockito.verify(intrusionDetectorManualMock, Mockito.times(1)).clean(Matchers.<Map<String, RecentLoginEntryContainer>> any(), Matchers.eq(intrusionTimer));
		Mockito.verify(intrusionDetectorMock, Mockito.times(1)).find(Matchers.<Map<String, RecentLoginEntryContainer>> any());
		Assert.assertNull(result);
	}

	@Test
	public void testParseLineNoFailure() {
		Mockito.when(intrusionDetectorMock.find(Matchers.<Map<String, RecentLoginEntryContainer>> any())).thenReturn(false);

		String result = analyzer.parseLine(successline);

		Mockito.verify(intrusionDetectorManualMock, Mockito.times(0)).clean(Matchers.<Map<String, RecentLoginEntryContainer>> any(), Matchers.anyLong());
		Mockito.verify(intrusionDetectorMock, Mockito.times(0)).find(Matchers.<Map<String, RecentLoginEntryContainer>> any());
		Assert.assertNull(result);
	}

}
