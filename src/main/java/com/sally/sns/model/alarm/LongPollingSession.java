package com.sally.sns.model.alarm;

import com.sally.sns.controller.response.Response;
import com.sally.sns.model.Member;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class LongPollingSession implements AlarmSession {
	private final Member recipient;
	private final DeferredResult<Response<List<Alarm>>> deferredResult;

	public static LongPollingSession of(Member recipient, DeferredResult<Response<List<Alarm>>> deferredResult) {
		return new LongPollingSession(recipient, deferredResult);
	}

	public boolean send(List<Alarm> alarms) {
		return this.deferredResult.setResult(Response.success(alarms));
	}

	public boolean sendError(Response response) {
		return this.deferredResult.setErrorResult(response);
	}

	public void isCompleted(Runnable callback) {
		this.deferredResult.onCompletion(callback);
	}

	@Override
	public Long recipientId() {
		return this.recipient.getUserId();
	}

	public boolean isExpiredOrNoResult() {
		return this.deferredResult.isSetOrExpired();
	}
}
