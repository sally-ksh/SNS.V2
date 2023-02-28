package com.sally.sns.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Sorry, we need a time."),
	DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated."),
	USER_NOT_FOUNDED(HttpStatus.NOT_FOUND, "No information of user."),
	INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "The password is wrong."),
	INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "The token is invalid."),
	INVALID_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "The user is unauthorized."),
	POST_NOT_FOUND(HttpStatus.NOT_FOUND, "The post is not found."),
	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "The comment in not found."),
	NO_TEXT(HttpStatus.LENGTH_REQUIRED, "The comment is blank."),
	ALARM_ERROR(HttpStatus.NOT_FOUND, "Error of sending the Alarm.");

	private HttpStatus httpStatus;
	private String message;
}
