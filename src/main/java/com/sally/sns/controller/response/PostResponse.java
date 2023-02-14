package com.sally.sns.controller.response;

import com.sally.sns.model.Post;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PostResponse {
	private final Long postId;
	private final String title;
	private final String content;
	private final UserResponse author;
	private final LocalDateTime createdAt;

	public static PostResponse of(Post post) {
		return PostResponse.builder()
			.postId(post.getPostId())
			.title(post.getTitle())
			.content(post.getContent())
			.author(UserResponse.of(post.getUser()))
			.createdAt(post.getCreatedAt())
			.build();
	}
}
