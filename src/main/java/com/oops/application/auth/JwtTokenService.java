package com.oops.application.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.oops.application.auth.model.TokenContext;
import com.oops.common.exception.ErrorCode;
import com.oops.common.exception.JwtException;
import com.oops.config.auth.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

	private final static String ACCESS_TOKEN_TYPE = "access";

	private final static String REFRESH_TOKEN_TYPE = "refresh";

	private final JwtConfig config;

	public TokenContext generateTokens(Long userId) {
		var now = Instant.now();
		var algorithm = Algorithm.HMAC256(config.secret());
		Instant accessTokenExpiresAt = now.plusSeconds(config.accessExp());
		Instant refreshTokenExpiresAt = now.plusSeconds(config.refreshExp());

		var accessToken = generateToken(userId, now, accessTokenExpiresAt, algorithm, ACCESS_TOKEN_TYPE);
		var refreshToken = generateToken(userId, now, refreshTokenExpiresAt, algorithm, REFRESH_TOKEN_TYPE);

		return new TokenContext(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
	}

	public DecodedJWT verifyAccessToken(String token) {
		try {
			return JWT.require(Algorithm.HMAC256(config.secret()))
				.withIssuer(config.issuer())
				.withAudience(config.audience())
				.withClaim("type", ACCESS_TOKEN_TYPE)
				.build()
				.verify(token);
		}
		catch (JWTVerificationException e) {
			throw new JwtException(ErrorCode.INVALID_ACCESS_TOKEN_ERROR);
		}
	}

	private String generateToken(Long userId, Instant now, Instant accessTokenExpiresAt, Algorithm algorithm,
			String tokenType) {
		return JWT.create()
			.withIssuer(config.issuer())
			.withAudience(config.audience())
			.withSubject(userId.toString())
			.withClaim("type", tokenType)
			.withIssuedAt(now)
			.withExpiresAt(accessTokenExpiresAt)
			.sign(algorithm);
	}

}
