package com.sally.sns.testEntity;

import com.sally.sns.model.UserRole;
import com.sally.sns.model.entity.UserEntity;

import java.time.LocalDateTime;

public class TestUserEntity {
	private static final Long USER_ENTITY_ID = 1l;
	private UserEntity userEntity;

	public static TestUserEntity of(String nickname, String password) {
		TestUserEntity testUserEntity = new TestUserEntity();
		testUserEntity.userEntity = new UserEntity(
			USER_ENTITY_ID,
			LocalDateTime.now(),
			LocalDateTime.now(),
			false,
			nickname,
			"tester@email.com",
			password,
			UserRole.USER);
		return testUserEntity;
	}

	public UserEntity toEntity() {
		return userEntity;
	}
}
