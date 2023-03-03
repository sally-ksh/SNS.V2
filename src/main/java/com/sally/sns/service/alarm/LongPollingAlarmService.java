package com.sally.sns.service.alarm;

import com.sally.sns.controller.response.Response;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.model.Member;
import com.sally.sns.model.alarm.Alarm;
import com.sally.sns.model.alarm.AlarmKeywordArgument;
import com.sally.sns.model.alarm.AlarmSession;
import com.sally.sns.model.alarm.AlarmType;
import com.sally.sns.model.alarm.LongPollingSession;
import com.sally.sns.model.entity.AlarmEntity;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.alarm.AlarmEntityRepository;
import com.sally.sns.repository.alarm.LongPollingAlarmInMemory;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Service
public class LongPollingAlarmService implements AlarmService {
	public final static long LONG_POLLING_TIMEOUT = 5000L;
	private final AlarmEntityRepository alarmEntityRepository;
	private final LongPollingAlarmInMemory alarmInMemory;

	@Scheduled(fixedRate = 1000)
	@Transactional
	public void send() {
		LongPollingSession session = alarmInMemory.isPeek();
		try {
			if (alarmInMemory.hasSession()) {
				List<AlarmEntity> alarmEntities = alarmEntityRepository.findAllByRecipientId(session.recipientId());
				if (alarmEntities.size() > 0) {
					session.send(
						alarmEntities.stream()
							.map(Alarm::from)
							.collect(Collectors.toUnmodifiableList()));
					alarmInMemory.remove();
					alarmEntities.stream().forEach(AlarmEntity::softlyDelete);
				}
			}
		} catch (Exception exception) {
			DeferredResult<Response<List<Alarm>>> deferredResult = session.getSender();
			deferredResult.setErrorResult(Response.error(ErrorCode.ALARM_ERROR.name()));
			alarmInMemory.remove();
		}
	}

	@Override
	public DeferredResult connect(AlarmSession alarmSession) {
		DeferredResult deferredResult = alarmSession.getSender();
		try {
			alarmInMemory.add((LongPollingSession)alarmSession);
			deferredResult.onCompletion(() -> log.info("The alarm publish is completed."));
		} catch (Exception exception) {
			deferredResult.setErrorResult(Response.error(ErrorCode.ALARM_ERROR.name()));
			alarmInMemory.remove();
		}

		deferredResult.onTimeout(() -> {
			deferredResult.setErrorResult(String.format("TimeOut: %dms", LONG_POLLING_TIMEOUT));
			alarmInMemory.remove();
		});
		return deferredResult;
	}

	@Transactional
	@Override
	public void storeCommentAlarm(Member member, AlarmKeywordArgument keywordArgument) {
		alarmEntityRepository.save(AlarmEntity.of(
			AlarmType.COMMENT,
			UserEntity.from(keywordArgument.getRecipientId()),
			keywordArgument));
	}
}

