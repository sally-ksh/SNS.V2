package com.sally.sns.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.fixture.entity.TestPostEntity;
import com.sally.sns.fixture.request.PostRequestFactory;
import com.sally.sns.service.PostService;
import com.sally.sns.testutil.TestMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PostApiController.class)
class PostApiControllerTest {
	public static final String TITLE = "titleTest";
	public static final String CONTENT = "contestTest";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	private TestPostEntity testPostEntity;

	@BeforeEach
	void beforeEach() {
		testPostEntity = TestPostEntity.of(TITLE, CONTENT);
	}

	@WithMockUser
	@Test
	@DisplayName("포스트 등록이 정상 동작 한다.")
	void create_post_ok() throws Exception {
		mockMvc.perform(post("/api/v2/sns/posts")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestMapper.content(getPostCreationRequest())))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@WithAnonymousUser
	@Test
	@DisplayName("포스트 등록시 비로그인 사용자 요청은 에러 발생 한다.")
	void create_invalidUser_error() throws Exception {
		doThrow(new SnsApplicationException(ErrorCode.INVALID_AUTHORIZATION))
			.when(postService).create(any(), anyString());

		mockMvc.perform(post("/api/v2/sns/posts")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestMapper.content(getPostCreationRequest())))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	private PostRequest.Creation getPostCreationRequest() {
		return PostRequestFactory.getPostCreationRequest(TITLE, CONTENT, PostRequestFactory.Designation.EMPTY);
	}

}
