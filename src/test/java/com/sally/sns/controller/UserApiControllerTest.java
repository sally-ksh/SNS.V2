package com.sally.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.service.UserService;

import org.junit.jupiter.api.DisplayName;
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
	private String email = "tester@email.com";
	private String password = "password1234";

	@Test
	@WithAnonymousUser
	@DisplayName("회원가입 요청 정상처리되어 OK 응답한다.")
	void join_valid_ok() throws Exception {
		doNothing().when(userService).create(any(UserRequest.Join.class));

		mockMvc.perform(post("/api/v2/users/join")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(nickName, password))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("회원가입 요청시 중복된 이메일 또는 닉네임은 에러발생 한다")
	void join_duplicatedEmailOrNickname_error() throws Exception {
		doThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "")).when(userService).create(any());

		mockMvc.perform(post("/api/v2/users/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(nickName, password))
			).andDo(print())
			.andExpect(status().isConflict());
		// .andExpect(status().is4xxClientError());
	}

	private String contentMapper(String nickName, String password) throws JsonProcessingException {
		return objectMapper.writeValueAsString(new UserRequest.Join(nickName, email, password));
	}
}
