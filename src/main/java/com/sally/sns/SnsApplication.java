package com.sally.sns;

import com.sally.sns.configuration.JwtConfiguration;
import com.sally.sns.configuration.KakaoRestApiProperty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(value = {KakaoRestApiProperty.class, JwtConfiguration.class})
@SpringBootApplication
public class SnsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnsApplication.class, args);
	}

}
