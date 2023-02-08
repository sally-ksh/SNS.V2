package com.sally.sns.service;

import static com.sally.sns.exception.ErrorCode.DUPLICATED_USER_NAME;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sally.sns.configuration.JwtConfiguration;
import com.sally.sns.controller.reuqest.UserRequest;
import com.sally.sns.exception.SnsApplicationException;
import com.sally.sns.model.entity.UserEntity;
import com.sally.sns.repository.UserEntityRepository;
import com.sally.sns.testEntity.TestUserEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks
	private UserService userService;
	@Mock
	private UserEntityRepository userEntityRepository;
	@Mock
	private BCryptPasswordEncoder passwordEncoder;
	@Mock
	private JwtConfiguration jwtConfiguration;

	private final String nickName = "nickName";
	private String email = "tester@email.com";
	private final String password = "password1234";

	private TestUserEntity testUserEntity;

	@BeforeEach
	void beforeEach() {
		testUserEntity = TestUserEntity.of(nickName, password);
	}

	@Test
	@DisplayName("사용자 저장은 정상 동작 한다.")
	void create_saveUser_ok() {
		when(userEntityRepository.existsUserByNickNameOrEmail(nickName, email)).thenReturn(false);
		when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

		userService.create(new UserRequest.Join(email, nickName, password));

		verify(userEntityRepository, times(1)).save(any(UserEntity.class));
		assertThatNoException();
	}

	@Test
	@DisplayName("사용자 등록은 닉네임 이나 이메일 중복되면 예외처리 한다.")
	void create_duplicatedNicknameOrEmail_ok() {
		when(userEntityRepository.existsUserByNickNameOrEmail(nickName, email)).thenReturn(true);

		assertThatThrownBy(() -> userService.create(new UserRequest.Join(email, nickName, password)))
			.isInstanceOf(SnsApplicationException.class)
			.hasMessageContaining(DUPLICATED_USER_NAME.name());
	}

	@Test
	@DisplayName("로그인이_정상_동작한다")
	void login_valid_ok() {
		String jwtSecretKey = "secretKey-size-more-than-256-bit";
		long jwtExpiredTime = 30 * 24 * 60 * 60 * 1000L;
		UserEntity userEntity = testUserEntity.toEntity();

		when(userEntityRepository.findByNickName(nickName)).thenReturn(Optional.of(userEntity));
		// when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
		when(userEntity.isMatchUpPassword(passwordEncoder, password)).thenReturn(true);
		when(jwtConfiguration.getSecretKey()).thenReturn(jwtSecretKey);
		when(jwtConfiguration.getExpiredTimeMs()).thenReturn(jwtExpiredTime);

		userService.login(new UserRequest.Login(nickName, password));

		assertThatNoException();
	}

}
