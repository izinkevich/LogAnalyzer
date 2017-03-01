package com.power;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.power.constants.IntrusionConstants;

/*  Shall be run as Java Application
 *  and stopped manually
 *  from the IDE
 */

public class AppITrunner {

	private final static int FAILURES_INIT_COUNT = 10;
	private static String logErrEntry1 = "30.212.19.124,1462471652,FAILURE,Thomas.Davenport\n";
	private static String logSuccessEntry = "30.212.127.12,1462472000,SUCCESS,Peter.Rascal\n";
	private static String logErrEntry2 = "30.212.19.124,1462472777,FAILURE,Thomas.Davenport\n";

	public static void main(String[] args) throws IOException, URISyntaxException {

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

		new App(logTempFile, new PrintStream(outputFile));

		waitFor(IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS / 2);
		Assert.assertEquals(FAILURES_INIT_COUNT - IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED + 1,
				Files.readLines(outputFile, Charsets.UTF_8).size());

		System.out.println("Append 2 failure lines and 1 success line to log");
		Files.append(logErrEntry1, logTempFile, Charsets.UTF_8);
		Files.append(logSuccessEntry, logTempFile, Charsets.UTF_8);
		Files.append(logErrEntry2, logTempFile, Charsets.UTF_8);

		waitFor(IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS);
		System.out.println("Check 2 more alarm messages in output");
		Assert.assertEquals(FAILURES_INIT_COUNT - IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED + 1 + 2,
				Files.readLines(outputFile, Charsets.UTF_8).size());

		System.out.println("Integration test PASSED, stop the application");
	}

	private static void waitFor(long millis) {
		System.out.println("Wait for " + millis + " milliseconds");
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
