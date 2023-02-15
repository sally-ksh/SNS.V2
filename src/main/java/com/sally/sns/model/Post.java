package com.sally.sns.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Post {
	private Long postId;
	private String title;
	private String content;
	private Member author;
	private LocalDateTime createdAt;
	private Location location;

	public static Post from(PostView postView) {
		return Post.builder()
			.postId(postView.getPostId())
			.title(postView.getTitle())
			.content(postView.getContent())
			.author(postView.getAuthor())
			.createdAt(postView.getCreatedAt())
			.location(postView.getLocation())
			.build();
	}
}
