package com.sally.sns.fixture.entity;

import com.sally.sns.model.Location;
import com.sally.sns.model.Post;
import com.sally.sns.model.entity.PostEntity;

import java.time.LocalDateTime;

public class TestPostEntity {
	private static final Long POST_ENTITY_ID = 1l;
	public static final Location location = Location.of("전북 익산시 부송동 100", 126.99597495347, 35.9766482774579);
	private PostEntity postEntity;

	public static TestPostEntity of(String title, String content, Location location) {
		TestPostEntity testPostEntity = new TestPostEntity();
		testPostEntity.postEntity = new PostEntity(
			POST_ENTITY_ID,
			LocalDateTime.now(),
			LocalDateTime.now(),
			false,
			title,
			content,
			TestUserEntity.getMember(),
			location
		);
		return testPostEntity;
	}

	public static TestPostEntity of(String title, String content) {
		return of(title, content, null);
	}

	public static TestPostEntity createEntityWithLocation(String title, String content) {
		TestPostEntity testPostEntity = new TestPostEntity();
		testPostEntity.postEntity = PostEntity.of(title, content, TestUserEntity.getMember(), location);
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
