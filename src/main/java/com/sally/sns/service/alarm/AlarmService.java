package com.sally.sns.service.alarm;

import com.sally.sns.model.Member;
import com.sally.sns.model.alarm.AlarmKeywordArgument;
import com.sally.sns.model.alarm.AlarmSession;

public interface AlarmService {
	<T> T connect(AlarmSession alarmSession);

	void storeCommentAlarm(Member member, AlarmKeywordArgument keywordArgument);
}
