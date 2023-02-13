package com.sally.sns.testutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestMapper {
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String content(Object request) throws JsonProcessingException {
		return objectMapper.writeValueAsString(request);
	}
}
