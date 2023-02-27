package com.sally.sns.service;

import com.sally.sns.model.AlarmKeywordArgument;
import com.sally.sns.model.Member;

public interface AlarmService {
	void getAlarms();

	void storeCommentAlarm(Member member, AlarmKeywordArgument keywordArgument);
}
