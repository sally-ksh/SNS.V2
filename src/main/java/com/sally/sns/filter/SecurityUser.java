package com.sally.sns.filter;

import org.springframework.security.core.AuthenticatedPrincipal;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SecurityUser implements AuthenticatedPrincipal, AuthenticationUser {
	private final AuthenticationUser user;

	@Override
	public String getName() {
		return user.getNickname();
	}

	@Override
	public Long getId() {
		return this.user.getId();
	}

	@Override
	public String getNickname() {
		return this.user.getNickname();
	}

	@Override
	public String getRole() {
		return this.user.getRole();
	}
}
