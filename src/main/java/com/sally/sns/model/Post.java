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
	private User user;
	private LocalDateTime createdAt;
}
