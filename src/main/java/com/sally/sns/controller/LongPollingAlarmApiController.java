package com.sally.sns.controller;

import static com.sally.sns.service.alarm.LongPollingAlarmService.LONG_POLLING_TIMEOUT;

import com.sally.sns.controller.response.AlarmResponse;
import com.sally.sns.controller.response.Response;
import com.sally.sns.model.Member;
import com.sally.sns.model.alarm.Alarm;
import com.sally.sns.model.alarm.LongPollingSession;
import com.sally.sns.service.alarm.AlarmService;
import com.sally.sns.service.alarm.LongPollingService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v2/sns/alarms")
public class LongPollingAlarmApiController extends AlarmRecipient {
	private static final ExecutorService executorService = Executors.newFixedThreadPool(5);
	private final AlarmService alarmService;

	public LongPollingAlarmApiController(@LongPollingService AlarmService alarmService) {
		this.alarmService = alarmService;
	}

	@GetMapping("/long-polling")
	public DeferredResult<?> publisher(Authentication authentication) {
		DeferredResult<Response<List<Alarm>>> output = new DeferredResult<>(LONG_POLLING_TIMEOUT);
		executorService.execute(() -> {
			final Member recipient = toMember(authentication);
			alarmService.connect(LongPollingSession.of(recipient, output));
		});
		output.onTimeout(() -> {
			log.info(AlarmResponse.TIME_OUT.name());
			alarmService.sendAndRemove(
				output.setErrorResult(AlarmResponse.TIME_OUT.make(String.valueOf(LONG_POLLING_TIMEOUT))));
		});
		return output;
	}
}
