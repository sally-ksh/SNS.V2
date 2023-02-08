package com.sally.sns.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sally.sns.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	private final int id;
	private final String nickName;
	private final String role;

	public static User fromEntity(UserEntity entity) {
		return new User(entity.getId(), entity.nickName(), entity.roleText());
	}
}
