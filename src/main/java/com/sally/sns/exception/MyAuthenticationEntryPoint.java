package com.sally.sns.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.sns.controller.response.Response;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(ErrorCode.INVALID_TOKEN.getHttpStatus().value());
		try (OutputStream os = response.getOutputStream()) {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(os, Response.error(ErrorCode.INVALID_TOKEN.name()));
			os.flush();
		}
	}
}
