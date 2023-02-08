package com.sally.sns.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sally.sns.model.UserRole;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

class JwtTokenUtilsTest {
	private final String secretKey = "secret-key";
	private final Algorithm jwtAlgorithm = Algorithm.HMAC256(secretKey);
	private final String testNickname = "sally";
	String token;

	@BeforeEach
	void beforeEach() {
		token = JWT.create()
			.withIssuer(testNickname)
			.withClaim("nickname", testNickname)
			.withClaim("role", UserRole.USER.name())
			.withExpiresAt(new Date(System.currentTimeMillis() + 1000))  // 실제는 만료기간 (30일)
			.sign(jwtAlgorithm);
	}

	@Test
	@DisplayName("토큰 만료기간 확인시 만료기간이 지나면 예외 발생한다")
	void auto0token_libraryTokenExpired_exception() {
		try {
			for (int i = 0; i < 2; i++) {
				Thread.sleep(2000);
			}
			DecodedJWT decodedToken = JWT.decode(token);
			JWTVerifier verifier = JWT.require(jwtAlgorithm)
				.withIssuer(decodedToken.getIssuer()) // specify an specific claim validations
				.build();

			assertThatThrownBy(() -> verifier.verify(token))
				.isInstanceOf(TokenExpiredException.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("토큰 기간 검증은 잘못된 토큰이나 key 전달시 예외 발생한다.")
	void token_invalidTokenOrSecretKey_error() {
		try {
			for (int i = 0; i < 2; i++) {
				Thread.sleep(2000);
			}
			assertThatThrownBy(() -> JwtTokenUtils.isExpired("wrongToken", secretKey))
				.isInstanceOf(JWTVerificationException.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("만료기간 지난 토큰은 기간 검증시 false 반환 한다.")
	void token_expired_isTrue() {
		try {
			for (int i = 0; i < 2; i++) {
				Thread.sleep(2000);
			}
			boolean actual = JwtTokenUtils.isExpired(token, secretKey);
			assertThat(actual).isFalse();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("토큰으로부터 사용자 정보를 가져온다.")
	void token_getUserName_ok() {
		String actual = JwtTokenUtils.getUserName(token, secretKey);

		assertThat(actual).isEqualTo(testNickname);
	}

	@Test
	@DisplayName("토큰은 만료기간이 지난 경우 정보조회시 검증 에러 발생한다.")
	void token_expiredGetUserName_error() {
		try {
			for (int i = 0; i < 2; i++) {
				Thread.sleep(2000);
			}
			assertThatThrownBy(() -> JwtTokenUtils.getUserName(token, secretKey))
				.isInstanceOf(JWTVerificationException.class);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
