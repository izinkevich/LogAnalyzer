package com.power;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.TimerTask;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.power.alarm.PrintAlarm;
import com.power.analyzer.LogAnalyzer;
import com.power.factory.LogAnalyzerFactory;


public class RepeatTask extends TimerTask {
	
	private File logfile;
	private PrintStream out;
	
	private static int countFailures = 0;

	public RepeatTask(final File logfile, final PrintStream out) {
		this.logfile = logfile;
		this.out = out;
	}
	
	public void run() {
		processLogForFailures();
	}

	private void processLogForFailures() {
		try {
			List<String> failureLogins = Files.readLines(logfile, Charsets.UTF_8, new LineProcessor<List<String>>() {
				List<String> result = Lists.newArrayList();

				public boolean processLine(String line) {
					if (line.contains("FAILURE")) {
						result.add(line.trim());
					}
					return true;
				}

				public List<String> getResult() {
					return result;
				}
			});
			int newCountFailures = failureLogins.size();
			if (countFailures < newCountFailures) {
				addNewEntriesToStorage(failureLogins.subList(countFailures, newCountFailures));
				countFailures = newCountFailures;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addNewEntriesToStorage(List<String> subList) {
		LogAnalyzer logAnalyzer = LogAnalyzerFactory.getInstance();
		for (String line : subList) {
			String intruderIp = logAnalyzer.parseLine(line);
			if (intruderIp != null) {
				PrintAlarm.print(out, "Attention! Possible intrusion from " + intruderIp);
			}
		}
	}
}