package com.sally.sns.service;

import com.sally.sns.model.AlarmKeywordArgument;
import com.sally.sns.model.Member;

public interface AlarmService {
	void send();

	void storeCommentAlarm(Member member, AlarmKeywordArgument keywordArgument);
}
