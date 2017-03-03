package com.power;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Timer;

import com.power.constants.IntrusionConstants;

public class App {
	
	private Timer timer;
	
	public App(File logfile, PrintStream out) {
		timer = new Timer();
		timer.schedule(new RepeatTask(logfile, out), 0, IntrusionConstants.DELAY_BETWEEN_FILE_READS_MILLIS);
	}
	
	private void cancelTimer() {
		timer.cancel();
	}
	
	@Override
	public void finalize() throws Throwable {
		cancelTimer();
		super.finalize();
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
