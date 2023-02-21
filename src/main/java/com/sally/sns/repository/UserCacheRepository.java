package com.sally.sns.repository;

import com.sally.sns.model.User;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserCacheRepository {
	public static final String PREFIX_USER = "USER-";
	private final static Duration USER_CACHE_TTL = Duration.ofDays(3);
	private final RedisTemplate<String, User> userRedisTemplate;  // 싱글 스레드

	public void save(User user) {
		String key = getKey(user.getNickname());
		log.info("store a user in a cache [{}:{}]", key, user);
		userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
	}

	public Optional<User> user(String nickname) {
		String key = getKey(nickname);
		User user = userRedisTemplate.opsForValue().get(key);
		log.info("read a user in the cache [{}:{}]", key, user);
		return Optional.ofNullable(user);
	}

	private String getKey(String nickname) {
		StringBuilder buffer = new StringBuilder();
		return buffer.append(PREFIX_USER)
			.append(nickname)
			.toString();
	}
}
