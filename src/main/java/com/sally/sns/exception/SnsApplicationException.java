package com.sally.sns.exception;

import org.springframework.http.HttpStatus;

import java.util.Objects;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SnsApplicationException extends RuntimeException {
	private final ErrorCode errorCode;
	private final String message;

	public SnsApplicationException(ErrorCode errorCode) {
		this.errorCode = errorCode;
		this.message = null;
	}

	public HttpStatus errorStatus() {
		return this.errorCode.getHttpStatus();
	}

	public String errorCode() {
		return this.errorCode.name();
	}

	@Override
	public String getMessage() {
		return Objects.isNull(message) ?
			errorCode.name() : String.format("%s, %s", errorCode.name(), message);
	}
}
