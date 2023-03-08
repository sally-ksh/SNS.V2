package com.sally.sns.repository.alarm;

import com.sally.sns.model.alarm.SseSession;

import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SseAlarmLocalInMemory {
	private static final String ALARM_PREFIX = "ALARM-COMMENT-USER:";
	private final Map<String, SseSession> broker = new ConcurrentHashMap<>();

	public void save(SseSession alarmSession) {
		final String key = getKey(alarmSession.recipientId());
		log.info("Store an alarm in a cache. [{}]", key);
		broker.put(key, alarmSession);
	}

	public Optional<SseSession> get(Long recipientId) {
		final String key = getKey(recipientId);
		log.info("Read an alarm in the cache. [{}]", key);
		return Optional.ofNullable(broker.get(key));
	}

	public void delete(Long recipientId) {
		final String key = getKey(recipientId);
		broker.remove(key);
	}

	private String getKey(Long recipientId) {
		StringBuilder buffer = new StringBuilder();
		return buffer.append(ALARM_PREFIX)
			.append(recipientId)
			.toString();
	}
}
