package com.sally.sns.service;

import com.sally.sns.model.Alarm;
import com.sally.sns.model.AlarmKeywordArgument;
import com.sally.sns.model.AlarmSession;
import com.sally.sns.model.AlarmType;
import com.sally.sns.model.Member;
import com.sally.sns.model.entity.AlarmEntity;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.alarm.AlarmEntityRepository;
import com.sally.sns.repository.alarm.LongPollingAlarmInMemory;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableScheduling
@RequiredArgsConstructor
@Service
public class LongPollingAlarmService implements AlarmService {
	private final AlarmEntityRepository alarmEntityRepository;
	private final LongPollingAlarmInMemory alarmInMemory;

	@Scheduled(fixedRate = 1000)
	@Transactional
	@Override
	public void getAlarms() {
		AlarmSession session = alarmInMemory.isPeek();
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

