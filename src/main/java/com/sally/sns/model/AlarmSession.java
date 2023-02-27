package com.sally.sns.model;

import com.sally.sns.controller.response.Response;

import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class AlarmSession {
	private final Member recipient;
	private final DeferredResult<Response<List<Alarm>>> deferredResult;

	public static AlarmSession of(Member recipient, DeferredResult<Response<List<Alarm>>> deferredResult) {
		return new AlarmSession(recipient, deferredResult);
	}

	public Long recipientId() {
		return this.recipient.getUserId();
	}

	public void send(List<Alarm> alarms) {
		this.deferredResult.setResult(Response.success(alarms));
	}

	public boolean isExpiredOrNoResult() {
		return this.deferredResult.isSetOrExpired();
	}
}
