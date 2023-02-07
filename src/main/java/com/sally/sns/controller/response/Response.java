package com.sally.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {
	public static String FORMAT = "{\nresultCode: %s \nresult: %s\n}";
	private String resultCode;
	private T result;

	public static Response<Void> error(String errorCode) {
		return new Response<>(errorCode, null);
	}

	public static <T> Response<T> success(T result) {
		return new Response<>("SUCCESS", result);
	}

	public static Response<Void> success() {
		return new Response<>("SUCCESS", null);
	}
}
