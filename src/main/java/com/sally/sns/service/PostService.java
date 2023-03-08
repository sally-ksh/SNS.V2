package com.sally.sns.service;

import com.sally.sns.controller.reuqest.CommentRequest;
import com.sally.sns.controller.reuqest.PostRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.model.Comment;
import com.sally.sns.model.Member;
import com.sally.sns.model.MyPost;
import com.sally.sns.model.Post;
import com.sally.sns.model.alarm.AlarmKeywordArgument;
import com.sally.sns.model.entity.CommentEntity;
import com.sally.sns.model.entity.PostEntity;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.CommentEntityRepository;
import com.sally.sns.repository.PostEntityRepository;
import com.sally.sns.service.alarm.AlarmService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final UserService userService;
	private final AlarmService alarmService;
	private final PostEntityRepository postEntityRepository;
	private final CommentEntityRepository commentEntityRepository;

	@Transactional
	public void create(PostRequest.Creation request, String nickname) {
		Member member = userService.getThatIfMember(nickname);
		PostEntity postEntity = PostEntity.of(
			request.getTitle(),
			request.getContent(),
			UserEntity.from(member.getUserId()),
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

	@Transactional
	public MyPost modify(PostRequest.Modification request, Long postId, Long userId) {
		PostEntity postEntity = getPostEntityWithUserOrThrow(postId);
		isAuthorOfThePost(userId, postEntity);
		postEntity.update(request.getTitle(), request.getContent());
		return MyPost.from(postEntity);
	}

	@Transactional
	public void deleteSoftly(Long postId, Long userId) {
		PostEntity postEntity = getPostEntityWithUserOrThrow(postId);
		isAuthorOfThePost(userId, postEntity);
		postEntity.softlyDelete();
	}

	@Transactional
	public void createComment(Long postId, CommentRequest.Creation request, String authorName) {
		PostEntity postEntity = getPostEntityOrThrow(postId);
		Member member = userService.getThatIfMember(authorName);
		CommentEntity commentEntity = CommentEntity.of(request.getBody(), UserEntity.from(member.getUserId()),
			postEntity);

		commentEntityRepository.save(commentEntity);
		AlarmKeywordArgument alarmKeywordArgument = AlarmKeywordArgument.addAlarmForComment(
			postEntity.authorId(), member.getUserId(), postEntity.getId(), commentEntity.getId());
		alarmService.storeCommentAlarm(alarmKeywordArgument);
	}

	@Transactional(readOnly = true)
	public Page<Comment> readComments(Long postId, Pageable pageable) {
		return commentEntityRepository.findCommentViewsBy(postId, pageable)
			.map(Comment::from);
	}

	@Transactional
	public String modifyComment(Long postId, Long commentId,
		CommentRequest.Modification request, Long userId) {
		CommentEntity commentEntity = getCommentEntityOrThrow(commentId);
		inValidPostOrCommenter(postId, commentId, userId, commentEntity);
		commentEntity.update(request.getBody());

		return commentEntity.content();
	}

	@Transactional
	public void deleteComment(Long postId, Long commentId, Long userId) {
		CommentEntity commentEntity = getCommentEntityOrThrow(commentId);
		inValidPostOrCommenter(postId, commentId, userId, commentEntity);

		commentEntity.deleteSoftly();
	}

	private PostEntity getPostEntityWithUserOrThrow(Long postId) {
		return postEntityRepository.findWithAuthorById(postId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
				String.format("The Post's ID : %d", postId)));
	}

	private PostEntity getPostEntityOrThrow(Long postId) {
		return postEntityRepository.findById(postId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND,
				String.format("The Post's ID : %d", postId)));
	}

	private void isAuthorOfThePost(Long userId, PostEntity postEntity) {
		if (postEntity.isNotAuthor(userId)) {
			throw new SnsApplicationException(ErrorCode.INVALID_AUTHORIZATION,
				String.format("PostService [Member: %d]", userId));
		}
	}

	private void inValidPostOrCommenter(Long postId, Long commentId, Long userId, CommentEntity commentEntity) {
		if (commentEntity.hasNotPost(postId)) {
			throw new SnsApplicationException(ErrorCode.INVALID_AUTHORIZATION,
				String.format("The Comment(id: %d) is existed, but No Post(id: %d).", commentId, postId));
		}
		if (commentEntity.isNotCommenter(userId)) {
			throw new SnsApplicationException(ErrorCode.INVALID_AUTHORIZATION,
				String.format("PostService - comment [Member: %d]", userId));
		}
	}

	private CommentEntity getCommentEntityOrThrow(Long commentId) {
		return commentEntityRepository.findById(commentId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.COMMENT_NOT_FOUND,
				String.format("The Comment ID : %d", commentId)));
	}
}
