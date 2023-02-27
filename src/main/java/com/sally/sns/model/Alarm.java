package com.sally.sns.model;

import com.sally.sns.model.entity.AlarmEntity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Alarm {
	private final Long recipientId;
	private final String message;
	private final AlarmKeywordArgument argument;
	private final LocalDateTime createdAt;

	public static Alarm from(AlarmEntity alarmEntity) {
		return Alarm.builder()
			.recipientId(alarmEntity.recipientId())
			.message(alarmEntity.typeText())
			.argument(alarmEntity.keywordArgument())
			.createdAt(alarmEntity.getCreatedAt())
			.build();
	}
}
