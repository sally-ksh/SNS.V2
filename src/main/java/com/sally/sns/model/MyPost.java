package com.sally.sns.model;

import com.sally.sns.model.entity.PostEntity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MyPost {
	private Long postId;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private Location location;

	public static MyPost from(PostEntity entity) {
		return MyPost.builder()
			.postId(entity.getId())
			.title(entity.title())
			.content(entity.content())
			.createdAt(entity.getCreatedAt())
			.location(entity.location())
			.build();
	}
}
