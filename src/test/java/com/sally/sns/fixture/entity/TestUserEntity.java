package com.sally.sns.fixture.entity;

import com.sally.sns.model.Member;
import com.sally.sns.model.UserRole;
import com.sally.sns.model.entity.UserEntity;

import java.time.LocalDateTime;

public class TestUserEntity {
	public static final Long USER_ENTITY_ID = 1l;
	public static final String NICK_NAME = "nicknameUser";
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

	public UserEntity getEntity() {
		return this.userEntity;
	}

	public static Member getMember() {
		return new Member(TestUserEntity.USER_ENTITY_ID, NICK_NAME);
	}
}
