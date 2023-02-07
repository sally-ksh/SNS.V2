package com.sally.sns.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry, we need a time."),
	DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated.");

	private HttpStatus httpStatus;
	private String message;
}
