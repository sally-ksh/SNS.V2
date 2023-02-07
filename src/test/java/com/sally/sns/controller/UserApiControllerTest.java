package com.sally.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserApiController.class,
	includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
class UserApiControllerTest {
	@Autowired
	private MockMvc mockMvc;

	// @Autowired
	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	private String nickName = "userID";
	private String password = "password1234";

	@Test
	@WithAnonymousUser
	void 회원가입() throws Exception {
		doNothing().when(userService).create(any(UserRequest.Join.class));

		mockMvc.perform(post("/api/v2/users/join")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(nickName, password))
			).andDo(print())
			.andExpect(status().isOk());
	}

	private String contentMapper(String nickName, String password) throws JsonProcessingException {
		return objectMapper.writeValueAsString(new UserRequest.Join(nickName, password));
	}
}
