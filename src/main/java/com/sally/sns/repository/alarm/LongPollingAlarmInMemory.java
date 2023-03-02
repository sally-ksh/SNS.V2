package com.sally.sns.repository.alarm;

import com.sally.sns.model.AlarmSession;

import org.springframework.stereotype.Repository;

import java.util.concurrent.LinkedBlockingDeque;

@Repository
public class LongPollingAlarmInMemory {
	private static LinkedBlockingDeque<AlarmSession> queue = new LinkedBlockingDeque(10);

	public void add(AlarmSession session) {
		queue.addLast(session);
	}

	public AlarmSession isPeek() {
		return queue.peekFirst();
	}

	public void remove() {
		queue.removeIf(session -> session.isExpiredOrNoResult());
	}

	public boolean hasSession() {
		return !queue.isEmpty();
	}
}
