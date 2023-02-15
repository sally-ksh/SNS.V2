package com.sally.sns.service;

import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
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

	public MyPost modify(PostRequest.Modification request, Long postId, Long userId) {
		PostEntity postEntity = getPostEntityOrThrow(postId);
		if (postEntity.isNotAuthor(userId)) {
			throw new SnsApplicationException(ErrorCode.INVALID_AUTHORIZATION,
				String.format("PostService : modify [Member: %s]", userId));
		}

		postEntity.update(request.getTitle(), request.getContent());
		return MyPost.from(postEntity);
	}

	private PostEntity getPostEntityOrThrow(Long postId) {
		return postEntityRepository.findWithAuthorById(postId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
				String.format("The Post's ID : %d", postId)));
	}
}
