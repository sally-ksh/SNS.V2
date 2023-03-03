package com.sally.sns.model.alarm;

import java.util.List;

public interface AlarmSession {
	Long recipientId();

	void send(List<Alarm> alarms);

	boolean isExpiredOrNoResult();

	<T> T getSender();
}
