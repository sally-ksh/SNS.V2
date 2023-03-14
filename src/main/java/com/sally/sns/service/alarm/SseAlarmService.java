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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Primary
@RequiredArgsConstructor
// @Service  // TODO
public class SseAlarmService implements AlarmService {
	public final static long SSE_TIMEOUT = 10L * 1000;
	private final AlarmEntityRepository alarmEntityRepository;
	private final SseAlarmLocalInMemory sseAlarmLocalInMemory;

	@Override
	public void connect(AlarmSession session) {
		SseSession sseSession = SseSession.class.cast(session);

		sseSession.checkEmitter(() -> sseAlarmLocalInMemory.delete(sseSession.recipientId()));

		sseAlarmLocalInMemory.save(sseSession);
		sseSession.send(ALARM_CONNECTION_MESSAGE, SseSession.Error.CONNECTION);
	}

	@Override
	public void storeCommentAlarm(AlarmKeywordArgument keywordArgument) {
		final Long recipientId = keywordArgument.getRecipientId();
		AlarmEntity alarmEntity = alarmEntityRepository.save(AlarmEntity.of(
			AlarmType.COMMENT,
			UserEntity.from(recipientId),
			keywordArgument));

		sseAlarmLocalInMemory.get(recipientId)
			.ifPresentOrElse(session -> {
				session.send(Alarm.from(alarmEntity), SseSession.Error.SEND);
				sseAlarmLocalInMemory.delete(recipientId);
			}, () -> log.info("The alarm is missed. [to: {}]", recipientId));
	}
}
