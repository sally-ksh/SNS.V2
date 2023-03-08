package com.sally.sns.service.alarm;

import com.sally.sns.controller.response.AlarmResponse;
import com.sally.sns.controller.response.Response;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.model.alarm.Alarm;
import com.sally.sns.model.alarm.AlarmKeywordArgument;
import com.sally.sns.model.alarm.AlarmSession;
import com.sally.sns.model.alarm.AlarmType;
import com.sally.sns.model.alarm.AlarmView;
import com.sally.sns.model.alarm.LongPollingSession;
import com.sally.sns.model.entity.AlarmEntity;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.alarm.AlarmEntityRepository;
import com.sally.sns.repository.alarm.LongPollingAlarmInMemory;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Service
public class LongPollingAlarmService implements AlarmService<DeferredResult<Response<List<Alarm>>>> {
	public static final long LONG_POLLING_TIMEOUT = 5000L;
	public static final String ERROR_LOG_OF_LONG_POLLING = "LongPollingAlarmService: {}";
	private final AlarmEntityRepository alarmEntityRepository;
	private final LongPollingAlarmInMemory alarmInMemory;

	public void send() {
		LongPollingSession session = alarmInMemory.isPeek();
		try {
			if (alarmInMemory.hasSession()) {
				List<AlarmView> alarmEntities = alarmEntityRepository.getAlarmsByRecipientId(session.recipientId());
				if (alarmEntities.size() > 0) {
					// updateForAlarmSendingStatus(alarmEntities);
					updateForAlarmSendingStatus(session.recipientId());
					List<Alarm> alarms = alarmEntities.stream()
						.map(Alarm::from)
						.collect(Collectors.toUnmodifiableList());
					this.sendAndRemove(session.send(alarms));
				}
			}
		} catch (Exception exception) {
			log.error(ERROR_LOG_OF_LONG_POLLING, exception.getMessage());
			sendAndRemove(session.sendError(AlarmResponse.ERROR.make(ErrorCode.ALARM_ERROR.name())));
		}
	}

	protected void updateForAlarmSendingStatus(Long recipientId) {
		int rows = alarmEntityRepository.updateAllSendStatusByRecipientId(recipientId);
		log.info("updated rows: {}", rows);
	}

	@Override
	public void connect(AlarmSession alarmSession) {
		Assert.notNull(alarmSession, "The alarm connection is not connected.");
		LongPollingSession longPollingSession = LongPollingSession.class.cast(alarmSession);
		try {
			alarmInMemory.add(longPollingSession);
			longPollingSession.isCompleted(() -> log.info("The alarm publish is completed."));
		} catch (Exception exception) {
			log.error(ERROR_LOG_OF_LONG_POLLING, exception.getMessage());
			sendAndRemove(longPollingSession.sendError(AlarmResponse.ERROR.make(ErrorCode.ALARM_ERROR.name())));
		}
	}

	@Transactional
	@Override
	public void storeCommentAlarm(AlarmKeywordArgument keywordArgument) {
		alarmEntityRepository.save(AlarmEntity.of(
			AlarmType.COMMENT,
			UserEntity.from(keywordArgument.getRecipientId()),
			keywordArgument));
	}

	@Override
	public void sendAndRemove(boolean isSend) {
		if (isSend) {
			alarmInMemory.remove();
		}
	}
}

