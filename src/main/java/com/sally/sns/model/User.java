package com.sally.sns.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sally.sns.model.entity.UserEntity;

import org.springframework.security.core.AuthenticatedPrincipal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements AuthenticatedPrincipal {
	private Long id;
	private String nickName;
	private String role;

	public static User fromEntity(UserEntity entity) {
		return new User(entity.getId(), entity.nickName(), entity.roleText());
	}

	@Override
	@JsonIgnore // exclude security property of redis
	public String getName() {
		return nickName;
	}
}
