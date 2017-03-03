package com.power;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.power.constants.IntrusionConstants;

public class AppITrunner {

	private final static int FAILURES_INIT_COUNT = 10;
	private static String logErrEntry1 = "30.212.19.124,1462471652,FAILURE,Thomas.Davenport\n";
	private static String logSuccessEntry = "30.212.127.12,1462472000,SUCCESS,Peter.Rascal\n";
	private static String logErrEntry2 = "30.212.19.124,1462472777,FAILURE,Thomas.Davenport\n";

	public static void main(String[] args) throws IOException, URISyntaxException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		URL logURL = AppITrunner.class.getClassLoader().getResource("log.txt");
		File logFile = new File(logURL.toURI());
		URL logTempURL = AppITrunner.class.getClassLoader().getResource("logTemp.txt");
		File logTempFile = new File(logTempURL.toURI());
		URL outputURL = AppITrunner.class.getClassLoader().getResource("output.txt");
		File outputFile = new File(outputURL.toURI());

		Assert.assertTrue(logFile.exists());
		Assert.assertTrue(logTempFile.exists());
		Assert.assertTrue(outputFile.exists());

		Files.write("", outputFile, Charsets.UTF_8);
		Files.copy(logFile, logTempFile);

		App app = new App(logTempFile, new PrintStream(outputFile));

		waitFor(IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS / 2);
		Assert.assertEquals(FAILURES_INIT_COUNT - IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED + 1, Files.readLines(outputFile, Charsets.UTF_8).size());

		System.out.println("Append 2 failure lines and 1 success line to log");
		Files.append(logErrEntry1, logTempFile, Charsets.UTF_8);
		Files.append(logSuccessEntry, logTempFile, Charsets.UTF_8);
		Files.append(logErrEntry2, logTempFile, Charsets.UTF_8);

		waitFor(IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS);
		System.out.println("Check 2 more alarm messages in output");
		Assert.assertEquals(FAILURES_INIT_COUNT - IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED + 1 + 2, Files.readLines(outputFile, Charsets.UTF_8).size());

		System.out.println("Append " + IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED + " more FAILURE messages after more than 5 minutes to log");
		for (int i = 1; i <= IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED; i++) {
			Files.append(addTime(logErrEntry2, IntrusionConstants.TIME_TO_STORE_MILLIS + i * 1000), logTempFile, Charsets.UTF_8);
		}

		waitFor(IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS);
		System.out.println("Check  - only one new alarm message is expected in output");
		Assert.assertEquals(FAILURES_INIT_COUNT - IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED + 1 + 3, Files.readLines(outputFile, Charsets.UTF_8).size());

		Method method = app.getClass().getDeclaredMethod("cancelTimer");
		method.setAccessible(true);
		method.invoke(app);
		
		System.out.println("Integration test PASSED");

	}

	private static void waitFor(long millis) {
		System.out.println("Wait for " + millis + " milliseconds");
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String addTime(String entry, long millis) {
		String[] parts = entry.split(",");
		Long newTime = Long.parseLong(parts[1]) + millis;
		parts[1] = newTime.toString();
		return parts[0] + ',' + parts[1] + ',' + parts[2] + ',' + parts[3];
	}

}
