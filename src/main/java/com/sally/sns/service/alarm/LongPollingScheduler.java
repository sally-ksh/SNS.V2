package com.sally.sns.service.alarm;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LongPollingScheduler {
	private final LongPollingAlarmService alarmService;

	@Scheduled(fixedRate = 1000)
	public void send() {
		alarmService.send();
	}
}
