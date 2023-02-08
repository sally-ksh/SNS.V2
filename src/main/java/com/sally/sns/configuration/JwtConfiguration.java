package com.sally.sns.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "jwt")
@ConstructorBinding
public class JwtConfiguration {
	private String secretKey;
	private Long expiredTimeMs;
}
