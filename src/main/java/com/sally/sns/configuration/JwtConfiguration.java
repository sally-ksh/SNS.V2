package com.sally.sns.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
public class JwtConfiguration {
	private final String secretKey;
	private final String expiredTimeMs;

	public String getSecretKey() {
		return secretKey;
	}

	public Long getExpiredTimeMs() {
		return Long.parseLong(expiredTimeMs);
	}
}
