package com.power.data;

public class LogEntry {
	
	private boolean isFailure = true;
	private String ipAddr;
	private long timeOfAccess;
	private String name;

	public boolean isFailure() {
		return isFailure;
	}
	public void setFailure(boolean isFailure) {
		this.isFailure = isFailure;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public long getTimeOfAccess() {
		return timeOfAccess;
	}
	public void setTimeOfAccess(long timeOfAccess) {
		this.timeOfAccess = timeOfAccess;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
