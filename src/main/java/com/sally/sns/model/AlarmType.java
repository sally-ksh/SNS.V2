package com.sally.sns.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {
	COMMENT("The new Comment."),
	POST_LIKE("The Like is added to the post."),
	COMMENT_LIKE("The Like is added to the comment."),
	AUTHOR_FOLLOW("The new Follow.");

	private final String message;
}
