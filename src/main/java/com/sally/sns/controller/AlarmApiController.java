package com.sally.sns.controller;

import static com.sally.sns.service.alarm.LongPollingAlarmService.LONG_POLLING_TIMEOUT;

import com.sally.sns.controller.response.Response;
import com.sally.sns.filter.AuthenticationUser;
import com.sally.sns.filter.SecurityUser;
import com.sally.sns.model.Member;
import com.sally.sns.model.alarm.Alarm;
import com.sally.sns.model.alarm.LongPollingSession;
import com.sally.sns.service.alarm.AlarmService;
import com.sally.sns.util.TypeCastingUtils;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v2/sns/alarms")
@RestController
public class AlarmApiController {
	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
	private final AlarmService alarmService;

	@GetMapping("/long-polling")
	public DeferredResult<?> publisher(Authentication authentication) {
		DeferredResult<Response<List<Alarm>>> output = new DeferredResult<>(LONG_POLLING_TIMEOUT);
		executorService.execute(() -> {
			final Member recipient = toMember(authentication);
			alarmService.connect(LongPollingSession.of(recipient, output));
		});
		return output;
	}

	@GetMapping("/sse/subscribe")
	public SseEmitter subscribe(Authentication authentication) {
		// final Member recipient = toMember(authentication);
		// return alarmService.connect(SseSession.of(recipient));
	}

	private Member toMember(Authentication authentication) {
		AuthenticationUser user = TypeCastingUtils.fromAndSecTo(
			authentication.getPrincipal(),
			SecurityUser.class,
			AuthenticationUser.class);
		return new Member(user.getId(), user.getNickname());
	}
}
