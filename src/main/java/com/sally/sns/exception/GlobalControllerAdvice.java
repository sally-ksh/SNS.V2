package com.sally.sns.exception;

import com.sally.sns.controller.response.Response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Response> exceptionHandler(Exception e) {
		log.error("error in server : {}", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(Response.error(ErrorCode.INTERNAL_SERVER_ERROR.name()));
	}

	@ExceptionHandler(SnsApplicationException.class)
	public ResponseEntity<Response> applicationHandler(SnsApplicationException snsException) {
		log.error("error of SNS : {}", snsException.toString());
		return ResponseEntity.status(snsException.errorStatus())
			.body(Response.error(snsException.errorCode()));
	}
}
