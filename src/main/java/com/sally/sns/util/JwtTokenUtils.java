package com.sally.sns.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sally.sns.model.UserRole;

import java.util.Date;

public abstract class JwtTokenUtils {
	public static final String TOKEN_ISSUER_NAME = "nickname";
	public static final String TOKEN_ROLE = "role";

	public static String generateToken(String nickName, String key, long expiredTimeMs) throws JWTCreationException {
		final Algorithm jwtAlgorithm = Algorithm.HMAC256(key);
		return JWT.create()
			.withIssuer(nickName)
			.withClaim(TOKEN_ISSUER_NAME, nickName)
			.withClaim(TOKEN_ROLE, UserRole.USER.name())
			.withExpiresAt(new Date(System.currentTimeMillis() + expiredTimeMs))  // 실제는 만료기간 (30일)
			.sign(jwtAlgorithm);
	}

	public static boolean isExpired(String token, String key) throws JWTVerificationException {
		Date expiredDate;
		try {
			expiredDate = extractClaims(token, key).getExpiresAt();
			return expiredDate.before(new Date());
		} catch (TokenExpiredException expiredException) {
			return false;
		}
	}

	private static DecodedJWT extractClaims(String token, String key) {
		final Algorithm jwtAlgorithm = Algorithm.HMAC256(key);
		DecodedJWT decodedToken = JWT.decode(token);
		JWTVerifier verifier = JWT.require(jwtAlgorithm)
			.withIssuer(decodedToken.getIssuer()) // specify an specific claim validations
			.build(); // reusable verifier instance
		return verifier.verify(token);
	}

	public static String getUserName(String token, String key) throws JWTVerificationException {
		try {
			return extractClaims(token, key).getClaim(TOKEN_ISSUER_NAME).asString();
		} catch (TokenExpiredException expiredException) {
			throw new JWTVerificationException("The token is expired.");
		}
	}
}
