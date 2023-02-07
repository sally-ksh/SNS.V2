package com.sally.sns.controller;

import com.sally.sns.controller.response.Response;
import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
@RestController
public class UserApiController {
	private final UserService userService;

	@PostMapping("/join")
	public Response join(@RequestBody UserRequest.Join userJoinRequest) {
		userService.create(userJoinRequest);
		return Response.success();
	}
}
