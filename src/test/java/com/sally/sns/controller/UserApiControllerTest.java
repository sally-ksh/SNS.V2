package com.sally.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.model.User;
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
		User mock = mock(User.class);
		when(userService.create(getJoinRequest())).thenReturn(mock);

		mockMvc.perform(post("/api/v2/users/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(getJoinRequest()))
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
				.content(contentMapper(getJoinRequest()))
			).andDo(print())
			.andExpect(status().isConflict());
		// .andExpect(status().is4xxClientError());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("로그인이 정상 동작한다.")
	void login_valid_OK() throws Exception {
		when(userService.login(getLoginRequest(password))).thenReturn("encoded-token");

		mockMvc.perform(post("/api/v2/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(getLoginRequest(password)))
			).andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("로그인시 가입 안 된 닉네임은 에러 처리한다.")
	void login_noneOfNickname_error() throws Exception {
		doThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED, "")).when(userService).login(any());

		mockMvc.perform(post("/api/v2/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(getLoginRequest(password)))
			).andDo(print())
			.andExpect(status().isNotFound());
	}

	@Test
	@WithAnonymousUser
	@DisplayName("로그인시 요청 비밀번호가 다르면 에러 처리한다.")
	void login_differentPassword_error() throws Exception {
		String wrongPassword = "wrongPwd1234";
		doThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD, "")).when(userService).login(any());

		mockMvc.perform(post("/api/v2/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(contentMapper(getLoginRequest(wrongPassword)))
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	private String contentMapper(Object request) throws JsonProcessingException {
		return objectMapper.writeValueAsString(request);
	}

	private UserRequest.Join getJoinRequest() {
		return new UserRequest.Join(email, nickName, password);
	}

	private UserRequest.Login getLoginRequest(String password) {
		return new UserRequest.Login(nickName, password);
	}
}
