package com.power.data;

import org.junit.Assert;

import org.junit.Test;

import com.power.constants.IntrusionConstants;

public class RecentLoginEntryContainerTest {

	RecentLoginEntryContainer container;
	String messageBufferTooSmall = "MAX_NUM_OF_ENTRIES_STORED is less than 5, that's too small!";

	@Test
	public void testRecentLoginEntryContainerLong() {
		container = new RecentLoginEntryContainer(1462123456l);
		Assert.assertTrue(container.size() == 1);
	}

	@Test
	public void testPushInto() {
		container = new RecentLoginEntryContainer(1462123456l);
		for (int i = 0; i < IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED - 2; i++) {
			container.pushInto(1462123457l);
		}
		Assert.assertEquals(IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED - 1, container.size());
	}

	@Test
	public void testPushIntoOverflow() {
		container = new RecentLoginEntryContainer();
		for (int i = 0; i < 2 * IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED; i++) {
			container.pushInto(1462123457l);
		}
		Assert.assertEquals(IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED, container.size());
	}

	@Test
	public void testDeleteOlderThan_Middle() {
		Assert.assertTrue(messageBufferTooSmall, IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED >= 5);
		container = new RecentLoginEntryContainer(1462123456l);
		container.pushInto(1462123457l);
		container.pushInto(1462123458l);
		container.pushInto(1462123459l);
		Assert.assertEquals(4, container.size());
		container.deleteOlderThan(1462123458l);
		Assert.assertEquals(2, container.size());
	}

	@Test
	public void testDeleteOlderThan_End() {
		Assert.assertTrue(messageBufferTooSmall, IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED >= 5);
		container = new RecentLoginEntryContainer();
		container.pushInto(1462123457l);
		container.pushInto(1462123458l);
		container.pushInto(1462123459l);
		Assert.assertEquals(3, container.size());
		container.deleteOlderThan(1462123459l);
		Assert.assertEquals(1, container.size());
	}

	@Test
	public void testDeleteOlderThan_BehindEnd() {
		Assert.assertTrue(messageBufferTooSmall, IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED >= 5);
		container = new RecentLoginEntryContainer();
		container.pushInto(1462123457l);
		container.pushInto(1462123458l);
		container.pushInto(1462123459l);
		Assert.assertEquals(3, container.size());
		container.deleteOlderThan(1462123465l);
		Assert.assertEquals(0, container.size());
	}

	@Test
	public void testDeleteOlderThan_Start() {
		Assert.assertTrue(messageBufferTooSmall, IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED >= 5);
		container = new RecentLoginEntryContainer(1462123456l);
		container.pushInto(1462123457l);
		container.pushInto(1462123458l);
		container.pushInto(1462123459l);
		Assert.assertEquals(4, container.size());
		container.deleteOlderThan(1462123456l);
		Assert.assertEquals(4, container.size());
	}

	@Test
	public void testDeleteOneOldestToNotRepeatAlarm_NotFull() {
		Assert.assertTrue(messageBufferTooSmall, IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED >= 5);
		container = new RecentLoginEntryContainer(1462123456l);
		container.pushInto(1462123458l);
		container.deleteOneOldestToNotRepeatAlarm();
		Assert.assertEquals(2, container.size());
	}

	@Test
	public void testDeleteOneOldestToNotRepeatAlarm_Full() {
		container = new RecentLoginEntryContainer();
		for (int i = 0; i < 2 * IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED; i++) {
			container.pushInto(1462123457l);
		}
		container.deleteOneOldestToNotRepeatAlarm();
		Assert.assertEquals(IntrusionConstants.MAX_NUM_OF_ENTRIES_STORED - 1, container.size());
	}

}
