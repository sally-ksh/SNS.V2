package com.sally.sns.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockAsyncContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
class AlarmApiControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@WithMockUser
	@Test
	@DisplayName("알람 조회 요청시 회원이 아니면 알람 에러 응답코드를 확인한다.")
	void publisher_notMember_errorCode() throws Exception {
		mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v2/sns/alarms/long-polling/testnoone").with(csrf()))
			.andExpect(status().isNotFound());
	}

	@WithMockUser
	@Test
	@DisplayName("알람 조회 요청시 알람이 없는 경우 타임아웃 응답을 확인한다.")
	void publisher_emptyMemberAlarms_timeout() throws Exception {
		MvcResult asyncListener = mockMvc.perform(
				MockMvcRequestBuilders.get("/api/v2/sns/alarms/long-polling/tester").with(csrf()))
			.andExpect(request().asyncStarted())
			.andReturn();

		enableTimeout(asyncListener);

		String response = mockMvc
			.perform(asyncDispatch(asyncListener))
			.andReturn()
			.getResponse()
			.getContentAsString();

		assertThat(response)
			.contains("TimeOut");
	}

	private static void enableTimeout(MvcResult asyncListener) throws IOException {
		((MockAsyncContext)asyncListener
			.getRequest()
			.getAsyncContext())
			.getListeners()
			.get(0)
			.onTimeout(null);
	}
}
