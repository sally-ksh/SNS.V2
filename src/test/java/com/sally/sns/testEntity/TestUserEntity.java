package com.sally.sns.testEntity;

import com.sally.sns.model.entity.UserEntity;

public class TestUserEntity {
	private static final Long USER_ENTITY_ID = 1l;
	private UserEntity userEntity;

	public static TestUserEntity of(String nickname, String password) {
		TestUserEntity testUserEntity = new TestUserEntity();
		testUserEntity.userEntity = UserEntity.of(nickname, "", password);
		return testUserEntity;
	}

	public UserEntity toEntity() {
		return userEntity;
	}
}
