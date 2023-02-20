package com.sally.sns.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class Comment {
	private final Long id;
	private final String body;
	private final Member member;
	private final LocalDateTime createdAt;

	public static Comment from(CommentView view) {
		return Comment.builder()
			.id(view.getId())
			.body(view.getComment())
			.member(view.getMember())
			.createdAt(view.getCreatedAt())
			.build();
	}
}
