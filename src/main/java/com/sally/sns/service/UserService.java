package com.sally.sns.service;

import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.model.User;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.UserEntityRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserEntityRepository userEntityRepository;
	private final BCryptPasswordEncoder passwordEncoder;

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

	private void isUniqueNickname(UserRequest.Join userJoinRequest) {
		if (userEntityRepository.existsUserByNickNameOrEmail(userJoinRequest.getNickname(),
			userJoinRequest.getEmail())) {
			throw new SnsApplicationException(
				ErrorCode.DUPLICATED_USER_NAME,
				String.format("%s is duplicated", userJoinRequest.getNickname()));
		}
	}
}
