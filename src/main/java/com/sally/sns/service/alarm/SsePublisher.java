package com.sally.sns.service.alarm;

import static com.sally.sns.model.alarm.SseSession.ALARM_CONNECTION_MESSAGE;

import com.sally.sns.model.alarm.Alarm;
import com.sally.sns.model.alarm.AlarmKeywordArgument;
import com.sally.sns.model.alarm.AlarmSession;
import com.sally.sns.model.alarm.AlarmType;
import com.sally.sns.model.alarm.SseSession;
import com.sally.sns.model.entity.AlarmEntity;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.alarm.AlarmEntityRepository;
import com.sally.sns.repository.alarm.SseAlarmLocalInMemory;
import com.sally.sns.util.TypeCastingUtils;

import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Primary
@RequiredArgsConstructor
@Service
public class SsePublisher implements AlarmService {
	private final RedisTemplate<String, Alarm> redisTemplate;
	private final RedisMessageListenerContainer redisMessageListenerContainer;
	private final SseSubscriber sseSubscriber;
	private final SseAlarmLocalInMemory sseAlarmLocalInMemory;
	private final AlarmEntityRepository alarmEntityRepository;

	@Override
	public void connect(AlarmSession alarmSession) {
		SseSession sseSession = TypeCastingUtils.fromAndTo(alarmSession, SseSession.class);
		// 토픽 수신자 userId
		redisMessageListenerContainer.addMessageListener(sseSubscriber, new ChannelTopic(sseSession.recipient()));
		sseSession.checkEmitter(() -> sseAlarmLocalInMemory.delete(sseSession.recipientId()));

		sseAlarmLocalInMemory.save(sseSession);
		sseSession.send(ALARM_CONNECTION_MESSAGE, SseSession.Error.CONNECTION);
	}

	@Override
	public void storeCommentAlarm(AlarmKeywordArgument keywordArgument) {
		final String recipient = keywordArgument.getRecipientId().toString();
		AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(
			AlarmType.COMMENT,
			UserEntity.from(keywordArgument.getRecipientId()),
			keywordArgument));

		redisTemplate.convertAndSend(recipient, Alarm.from(alarmEntity));
	}
}
