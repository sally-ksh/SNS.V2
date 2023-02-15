package com.sally.sns.controller.response;

import static lombok.AccessLevel.PRIVATE;

import com.sally.sns.model.MyPost;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = PRIVATE)
@AllArgsConstructor
public class MyPostResponse {
	private final Long postId;
	private final String title;
	private final String content;
	private final LocalDateTime createdAt;

	public static MyPostResponse of(MyPost myPost) {
		return MyPostResponse.builder()
			.postId(myPost.getPostId())
			.title(myPost.getTitle())
			.content(myPost.getContent())
			.createdAt(myPost.getCreatedAt())
			.build();
	}
}
