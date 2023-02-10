package com.sally.sns.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kakao")
@ConstructorBinding
public class KakaoRestApiProperty {
	private final String restApiKey;
}
