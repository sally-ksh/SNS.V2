package com.sally.sns.controller;

import com.sally.sns.controller.response.MyPostResponse;
import com.sally.sns.controller.response.PostResponse;
import com.sally.sns.controller.response.Response;
import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.model.User;
import com.sally.sns.service.PostService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	@GetMapping
	public Response<Page<PostResponse>> getLists(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		Authentication authentication) {
		return Response.success(postService.readAll(pageable).map(PostResponse::of));
	}

	@GetMapping("/my")
	public Response<Page<MyPostResponse>> getMyList(
		@PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		Authentication authentication) {
		User user = (User)authentication.getPrincipal();
		return Response.success(postService.readMemberPosts(pageable, user.getId()).map(MyPostResponse::of));
	}

	@PutMapping("/{postId}")
	public Response<MyPostResponse> modify(@PathVariable Long postId,
		@RequestBody PostRequest.Modification request, Authentication authentication) {
		User user = (User)authentication.getPrincipal();
		return Response.success(MyPostResponse.of(postService.modify(request, postId, user.getId())));
	}

	@DeleteMapping("/{postId}")
	public Response delete(@PathVariable Long postId, Authentication authentication) {
		User user = (User)authentication.getPrincipal();
		postService.deleteSoftly(postId, user.getId());
		return Response.success();
	}
}
