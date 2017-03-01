package com.power.data;

import java.util.Queue;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.Queues;
import com.power.constants.IntrusionConstants;

public class RecentLoginEntryContainer {

	private EvictingQueue<Long> timeValues;
	private Queue<Long> times;	

	public RecentLoginEntryContainer() {
		this.timeValues = EvictingQueue.create(IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED);
		this.times =  Queues.synchronizedQueue(timeValues);
	}

	public RecentLoginEntryContainer(long time) {
		this();
		times.add(time);
	}
	
	public void pushInto(long time) {
		times.add(time);
	}

	public void deleteOlderThan(long olderThan) {
		Long value = times.peek();
		while(value != null) {
			if (value < olderThan) {
				times.poll();
			} else {
				break;
			}
			value = times.peek();
		}
	}
	
	public void deleteOneOldestToNotRepeatAlarm() {
		if (size() == IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED) {
			times.poll();
		}
	}
	
	public int size() {
		return times.size();
	}
}
