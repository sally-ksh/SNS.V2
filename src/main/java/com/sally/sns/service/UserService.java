package com.sally.sns.service;

import com.sally.sns.configuration.JwtConfiguration;
import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.model.User;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.UserCacheRepository;
import com.sally.sns.repository.UserEntityRepository;
import com.sally.sns.util.JwtTokenUtils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserEntityRepository userEntityRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final JwtConfiguration jwtConfiguration;
	private final UserCacheRepository userCacheRepository;

	@Transactional
	public User create(UserRequest.Join request) {
		isUniqueNickname(request);
		UserEntity userEntity = userEntityRepository.save(
			UserEntity.of(
				request.getNickname(),
				request.getEmail(),
				passwordEncoder.encode(request.getPassword())
			));
		return User.fromEntity(userEntity);
	}

	@Transactional(readOnly = true)
	public String login(UserRequest.Login userLoginRequest) {
		if (getUserFromCache(userLoginRequest.getNickname()).isPresent()) {
			return toJwtToken(userLoginRequest);
		}
		UserEntity userEntity = getUserEntityOrThrow(userLoginRequest.getNickname());
		if (!userEntity.isMatchUpPassword(passwordEncoder, userLoginRequest.getPassword())) {
			throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
		}
		userCacheRepository.save(User.fromEntity(userEntity));
		return toJwtToken(userLoginRequest);
	}

	@Transactional(readOnly = true)
	public User loadUserByUserName(String nickName) {
		return getUserFromCache(nickName)
			.orElse(User.fromEntity(getUserEntityOrThrow(nickName)));
	}

	private void isUniqueNickname(UserRequest.Join userJoinRequest) {
		if (userEntityRepository.existsUserByNickNameOrEmail(userJoinRequest.getNickname(),
			userJoinRequest.getEmail())) {
			throw new SnsApplicationException(
				ErrorCode.DUPLICATED_USER_NAME,
				String.format("%s is duplicated", userJoinRequest.getNickname()));
		}
	}

	private UserEntity getUserEntityOrThrow(String nickName) {
		return userEntityRepository.findByNickName(nickName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUNDED,
				String.format("%s - %s", this.getClass().getSimpleName(), "getUserEntityOrThrow")));
	}

	private String toJwtToken(UserRequest.Login userLoginRequest) {
		return JwtTokenUtils.generateToken(
			userLoginRequest.getNickname(),
			jwtConfiguration.getSecretKey(),
			jwtConfiguration.getExpiredTimeMs());
	}

	private Optional<User> getUserFromCache(String nickname) {
		return userCacheRepository.user(nickname);
	}
}
