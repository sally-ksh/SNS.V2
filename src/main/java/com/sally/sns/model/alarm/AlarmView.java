package com.sally.sns.model.alarm;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class AlarmView {
	private final Long recipientId;
	private final AlarmType type;
	private final AlarmKeywordArgument keywordArgument;
	private final LocalDateTime createdAt;

	public AlarmView(Long recipientId, AlarmType type, Object keywordArgument,
		LocalDateTime createdAt) {
		AlarmKeywordArgument alarmKeywordArgument = (AlarmKeywordArgument)keywordArgument;
		this.recipientId = recipientId;
		this.type = type;
		this.keywordArgument = alarmKeywordArgument;
		this.createdAt = createdAt;
	}

	public String typeText() {
		return this.type.getMessage();
	}
}
