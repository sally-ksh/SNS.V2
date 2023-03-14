package com.sally.sns.controller;

import static com.sally.sns.service.alarm.SseAlarmService.SSE_TIMEOUT;

import com.sally.sns.model.alarm.SseSession;
import com.sally.sns.service.alarm.AlarmService;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v2/sns/alarms")
public class AlarmApiController extends AlarmRecipient {
	private final AlarmService alarmService;

	public AlarmApiController(AlarmService alarmService) {
		this.alarmService = alarmService;
	}

	@GetMapping(value = "/sse/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe(Authentication authentication) {
		SseEmitter sseEmitter = new SseEmitter(SSE_TIMEOUT);

		final Long userId = toMember(authentication).getUserId();
		alarmService.connect(SseSession.of(userId, sseEmitter));
		return sseEmitter;
	}
}
