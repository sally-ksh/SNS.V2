package com.sally.sns.fixture.entity;

import static com.sally.sns.fixture.entity.TestUserEntity.NICK_NAME;
import static com.sally.sns.fixture.entity.TestUserEntity.USER_ENTITY_ID;

import com.sally.sns.model.Location;
import com.sally.sns.model.Post;
import com.sally.sns.model.entity.PostEntity;
import com.sally.sns.model.entity.UserEntity;

import java.time.LocalDateTime;

public class TestPostEntity {
	private static final Long POST_ENTITY_ID = 1l;
	public static final Location location = Location.of("전북 익산시 부송동 100", 126.99597495347, 35.9766482774579);
	private static UserEntity userEntity = TestUserEntity.of(NICK_NAME, "password1234").getEntity();
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
			userEntity,
			location
		);
		return testPostEntity;
	}

	public static TestPostEntity of(String title, String content) {
		return of(title, content, null);
	}

	public static TestPostEntity createEntityWithLocation(String title, String content) {
		TestPostEntity testPostEntity = new TestPostEntity();
		testPostEntity.postEntity = PostEntity.of(title, content, userEntity, location);
		return testPostEntity;
	}

	public PostEntity toEntity() {
		return postEntity;
	}

	public Long postId() {
		return this.postEntity.getId();
	}

	public Long authorId() {
		return USER_ENTITY_ID;
	}

	public String getNickname() {
		return userEntity.nickName();
	}

	public Post getPostDto() {
		return Post.builder()
			.postId(postEntity.getId())
			.build();
	}
}
