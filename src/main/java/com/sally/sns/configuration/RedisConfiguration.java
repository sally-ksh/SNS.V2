package com.sally.sns.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sally.sns.model.User;
import com.sally.sns.model.alarm.Alarm;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableRedisRepositories
@Configuration
public class RedisConfiguration {
	private final RedisProperties redisProperties;
	private final ObjectMapper objectMapper;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory(redisProperties.getHost(), redisProperties.getPort());
	}

	@Bean
	public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		Jackson2JsonRedisSerializer<User> serializer = getSerializer(User.class);

		RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, Alarm> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		Jackson2JsonRedisSerializer<Alarm> serializer = getSerializer(Alarm.class);
		
		RedisTemplate<String, Alarm> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);

		redisTemplate.setKeySerializer(new StringRedisSerializer());  // TODO 확인해보기
		redisTemplate.setValueSerializer(serializer);
		return redisTemplate;
	}

	@Bean
	RedisMessageListenerContainer redisMessageListenerContainer() {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory());
		return container;
	}

	private <T> Jackson2JsonRedisSerializer<T> getSerializer(Class<T> clazz) {
		var serializer = new Jackson2JsonRedisSerializer<>(clazz);
		serializer.setObjectMapper(objectMapper);
		return serializer;
	}
}
