package com.sally.sns.controller.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AlarmResponse {
	TIME_OUT("TimeOut: %sms"), ERROR("[ALARM] %s");

	private final String message;

	public Response make(String information) {
		String formattedMessage = String.format(this.message, information);
		switch (this) {
			case ERROR:
				return Response.error(formattedMessage);
			case TIME_OUT:
				return Response.success(formattedMessage);
			default:
				return Response.success();
		}
	}
}
