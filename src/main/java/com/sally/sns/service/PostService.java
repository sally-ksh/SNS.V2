package com.sally.sns.service;

import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.model.MyPost;
import com.sally.sns.model.Post;
import com.sally.sns.model.User;
import com.sally.sns.model.entity.PostEntity;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.PostEntityRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
			UserEntity.from(user.getId()),
			request.getPlace().toLocation());

		postEntityRepository.save(postEntity);
	}

	@Transactional(readOnly = true)
	public Page<Post> readAll(Pageable pageable) {
		return postEntityRepository.findPostViews(pageable).map(Post::from);
	}

	@Transactional(readOnly = true)
	public Page<MyPost> readMemberPosts(Pageable pageable, Long userId) {
		return postEntityRepository.findAllByAuthorId(userId, pageable)
			.map(MyPost::from);
	}
}
