package com.sally.sns.model;

import com.sally.sns.filter.AuthenticationUser;
import com.sally.sns.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User implements AuthenticationUser {
	private Long id;
	private String nickname;
	private String role;

	public static User fromEntity(UserEntity entity) {
		return new User(entity.getId(), entity.nickName(), entity.roleText());
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String getNickname() {
		return this.nickname;
	}

	@Override
	public String getRole() {
		return this.role;
	}
}
