package com.sally.sns.repository.alarm;

import com.sally.sns.model.alarm.LongPollingSession;

import org.springframework.stereotype.Repository;

import java.util.concurrent.LinkedBlockingDeque;

@Repository
public class LongPollingAlarmInMemory {
	private static LinkedBlockingDeque<LongPollingSession> queue = new LinkedBlockingDeque(10);

	public void add(LongPollingSession session) {
		queue.addLast(session);
	}

	public LongPollingSession isPeek() {
		return queue.peekFirst();
	}

	public void remove() {
		queue.removeIf(session -> session.isExpiredOrNoResult());
	}

	public boolean hasSession() {
		return !queue.isEmpty();
	}
}
