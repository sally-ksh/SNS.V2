package com.sally.sns.testEntity;

import com.sally.sns.model.Post;
import com.sally.sns.model.entity.PostEntity;

import java.time.LocalDateTime;

public class TestPostEntity {
	private static final Long POST_ENTITY_ID = 1l;
	private PostEntity postEntity;

	public static TestPostEntity of(String title, String content) {
		TestPostEntity testPostEntity = new TestPostEntity();
		testPostEntity.postEntity = new PostEntity(
			POST_ENTITY_ID,
			LocalDateTime.now(),
			LocalDateTime.now(),
			false,
			title,
			content,
			TestUserEntity.USER_ENTITY_ID
		);
		return testPostEntity;
	}

	public PostEntity toEntity() {
		return postEntity;
	}

	public Post getPostDto() {
		return Post.builder()
			.postId(postEntity.getId())
			.build();
	}
}
