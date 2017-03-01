package com.power.analyzer;

public interface LogAnalyzer {
	/*
	* Line format
	*30.212.19.124,1462457652,FAILURE,Thomas.Davenport
	*30.212.127.12,1462458822,SUCCESS,Peter.Rascal
	*30.212.19.124,1462459026,FAILURE,Thomas.Davenport
	*/
	 String parseLine(String line);
}
