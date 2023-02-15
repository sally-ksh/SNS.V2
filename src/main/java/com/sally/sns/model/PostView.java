package com.sally.sns.model;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class PostView {
	private final Long postId;
	private final String title;
	private final String content;
	private final Member author;
	private final Location location;
	private final LocalDateTime createdAt;

	public PostView(Long postId, String title, String content, Long userId, String userName,
		Location location, LocalDateTime createdAt) {
		this.postId = postId;
		this.title = title;
		this.content = content;
		this.author = new Member(userId, userName);
		this.location = location;
		this.createdAt = createdAt;
	}
}
