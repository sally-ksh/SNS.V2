package com.sally.sns.service;

import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.model.Member;
import com.sally.sns.model.User;
import com.sally.sns.model.entity.PostEntity;
import com.sally.sns.repository.PostEntityRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostEntityRepository postEntityRepository;
	private final UserService userService;

	@Transactional
	public void create(PostRequest.Creation request, String nickname) {
		User user = userService.loadUserByUserName(nickname);
		PostEntity postEntity = PostEntity.of(
			request.getTitle(),
			request.getContent(),
			new Member(user.getId(), user.getNickName()),
			request.getPlace().toLocation());

		postEntityRepository.save(postEntity);
	}
}
