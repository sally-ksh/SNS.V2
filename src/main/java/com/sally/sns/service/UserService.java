package com.sally.sns.service;

import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.exception.ErrorCode;
import com.sally.sns.exception.SnsApplicationException;
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
	public void create(UserRequest.Join request) {
		isUniqueNickname(request);
		userEntityRepository.save(
			UserEntity.of(
				request.getNickname(),
				passwordEncoder.encode(request.getPassword())
			));
	}

	private void isUniqueNickname(UserRequest.Join userJoinRequest) {
		if (userEntityRepository.existsUserByNickName(userJoinRequest.getNickname())) {
			throw new SnsApplicationException(
				ErrorCode.DUPLICATED_USER_NAME,
				String.format("%s is duplicated", userJoinRequest.getNickname()));
		}
	}
}
