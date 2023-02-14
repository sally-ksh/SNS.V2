package com.sally.sns.controller;

import com.sally.sns.controller.response.Response;
import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.service.PostService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/sns/posts")
public class PostApiController {
	private final PostService postService;

	@PostMapping
	public Response write(@RequestBody PostRequest.Creation request, Authentication authentication) {
		postService.create(request, authentication.getName());
		return Response.success();
	}
}
