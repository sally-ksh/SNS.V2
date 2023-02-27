package com.sally.sns.controller;

import com.sally.sns.controller.response.Response;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.model.Alarm;
import com.sally.sns.model.AlarmSession;
import com.sally.sns.model.Member;
import com.sally.sns.repository.alarm.LongPollingAlarmInMemory;
import com.sally.sns.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

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
	private final LongPollingAlarmInMemory alarmInMemory;
	public final static Long LONG_POLLING_TIMEOUT = 5000L;  // 5000L
	private final UserService userService;

	@GetMapping("/long-polling/{nickname}")
	public DeferredResult<Response<List<Alarm>>> publisher(
		@PathVariable String nickname) {
		DeferredResult<Response<List<Alarm>>> output = new DeferredResult<>(LONG_POLLING_TIMEOUT);

		executorService.execute(() -> {
			try {
				Member member = userService.getThatIfMember(nickname);
				alarmInMemory.add(AlarmSession.of(member, output));
				output.onCompletion(() -> log.info("The alarm publish is completed."));
			} catch (Exception exception) {
				output.setErrorResult(Response.error(ErrorCode.ALARM_ERROR.name()));
				alarmInMemory.remove();
			}
		});
		output.onTimeout(() -> {
			output.setErrorResult(String.format("TimeOut: %dms", LONG_POLLING_TIMEOUT));
			alarmInMemory.remove();
		});
		return output;
	}
}
