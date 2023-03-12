package com.sally.sns.model.alarm;

import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@AllArgsConstructor
public class SseSession implements AlarmSession {
	public static final String ALARM_CONNECTION_MESSAGE = "The connection is completed";
	private final long userId;
	private SseEmitter sseEmitter;

	public static SseSession of(Long recipientId, SseEmitter sseEmitter) {
		return new SseSession(recipientId, sseEmitter);
	}

	@Override
	public Long recipientId() {
		return this.userId;
	}

	public String recipient() {
		return String.valueOf(this.userId);
	}

	public void checkEmitter(Runnable runnable) {
		this.sseEmitter.onCompletion(runnable);
		this.sseEmitter.onTimeout(runnable);
	}

	public void send(Object data, Error error) {
		try {
			sseEmitter.send(SseEmitter.event().id("").name("alarm").data(data));
		} catch (IOException exception) {
			throw new SnsApplicationException(ErrorCode.ALARM_ERROR, error.getMessage());
		}
	}

	@Getter
	@AllArgsConstructor
	public enum Error {
		CONNECTION("The connect is fail."), SEND("The pushing is fail.");
		private String message;
	}
}
