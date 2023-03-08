package com.sally.sns.model.alarm;

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

	public static Alarm from(AlarmView alarmView) {
		return Alarm.builder()
			.recipientId(alarmView.getRecipientId())
			.message(alarmView.typeText())
			.argument(alarmView.getKeywordArgument())
			.createdAt(alarmView.getCreatedAt())
			.build();
	}

	public static Alarm from(AlarmEntity entity) {
		return Alarm.builder()
			.recipientId(entity.recipientId())
			.message(entity.typeText())
			.argument(entity.keywordArgument())
			.createdAt(entity.getCreatedAt())
			.build();
	}
}
