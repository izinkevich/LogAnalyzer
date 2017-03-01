package com.power;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Timer;

import com.power.constants.IntrusionConstants;

public class App {
	
	public App(File logfile, PrintStream out) {
		new Timer().schedule(new RepeatTask(logfile, out), 0, IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS);
	}

	public static void main(String[] args) throws FileNotFoundException {
		if (args.length > 0) {
			File logFile = new File(args[0]);
			if (logFile.exists()) {
				new App(logFile, System.err);
			} else {
				System.out.println("Put correct file name!");
			}
		} else {
			System.out.println("Put file name as an argument!");
		}
	}

}
