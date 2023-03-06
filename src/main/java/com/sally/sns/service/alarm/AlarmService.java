package com.sally.sns.service.alarm;

import com.sally.sns.model.alarm.AlarmKeywordArgument;
import com.sally.sns.model.alarm.AlarmSession;

public interface AlarmService<T> {
	void connect(AlarmSession alarmSession);

	void storeCommentAlarm(AlarmKeywordArgument keywordArgument);

	default void sendAndRemove(boolean sender) {
		
	}
}
