package com.sally.sns.service;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.UserEntityRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks
	private UserService userService;
	@Mock
	private UserEntityRepository userEntityRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	// @Mock
	// private JwtConfiguration jwtConfiguration;

	private final String nickName = "nickName";
	private final String password = "password1234";

	@Test
	void 사용자_등록이_정상_동작한다() {
		when(userEntityRepository.existsUserByNickName(nickName)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

		userService.create(new UserRequest.Join(nickName, password));

		verify(userEntityRepository, times(1)).save(any(UserEntity.class));
		assertThatNoException();
	}

}
