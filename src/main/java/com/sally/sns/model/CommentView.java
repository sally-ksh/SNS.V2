package com.sally.sns.model;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class CommentView {
	private final Long id;
	private final String comment;
	private final Member member;
	private final LocalDateTime createdAt;

	public CommentView(Long id, String comment, LocalDateTime createdAt, Long userId, String nickname) {
		this.id = id;
		this.comment = comment;
		this.member = new Member(userId, nickname);
		this.createdAt = createdAt;
	}
}
