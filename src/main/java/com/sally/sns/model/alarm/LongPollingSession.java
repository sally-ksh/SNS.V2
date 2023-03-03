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

	@Override
	public Long recipientId() {
		return this.recipient.getUserId();
	}

	@Override
	public void send(List<Alarm> alarms) {
		this.deferredResult.setResult(Response.success(alarms));
	}

	@Override
	public boolean isExpiredOrNoResult() {
		return this.deferredResult.isSetOrExpired();
	}

	@Override
	public DeferredResult<Response<List<Alarm>>> getSender() {
		return this.deferredResult;
	}
}
