package com.sally.sns.controller;

import com.sally.sns.filter.AuthenticationUser;
import com.sally.sns.filter.SecurityUser;
import com.sally.sns.model.Member;
import com.sally.sns.util.TypeCastingUtils;

import org.springframework.security.core.Authentication;

public abstract class AlarmRecipient {
	private AuthenticationUser toAuthenticationUser(Authentication authentication) {
		return TypeCastingUtils.fromAndSecTo(
			authentication.getPrincipal(),
			SecurityUser.class,
			AuthenticationUser.class);
	}

	protected Member toMember(Authentication authentication) {
		AuthenticationUser user = toAuthenticationUser(authentication);
		return new Member(user.getId(), user.getNickname());
	}
}
