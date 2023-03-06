package com.sally.sns.model.alarm;

public interface AlarmSession {
	Long recipientId();

	boolean isExpiredOrNoResult();

	<T> T getSender();
}
