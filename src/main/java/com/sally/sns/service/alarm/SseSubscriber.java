package com.sally.sns.service.alarm;

import com.sally.sns.model.alarm.SseSession;
import com.sally.sns.repository.alarm.SseAlarmLocalInMemory;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseSubscriber implements MessageListener {
	private final SseAlarmLocalInMemory sseAlarmLocalInMemory;

	@Override
	public void onMessage(Message message, byte[] pattern) {
		final var subscriber = getSubscriberFrom(message);
		sseAlarmLocalInMemory.get(subscriber)
			.ifPresentOrElse(session -> {
				session.send(message.toString(), SseSession.Error.SEND);
				session.checkEmitter(() -> sseAlarmLocalInMemory.delete(subscriber));
			}, () -> log.info("Because The emitter is Expired. The alarm is missed. [to: {}]", subscriber));
	}

	private Long getSubscriberFrom(Message message) {
		return NumberUtils.parseNumber(new String(message.getChannel()), Long.class);
	}
}
