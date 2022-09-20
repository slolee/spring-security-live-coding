package com.example.dammpractice.helper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import com.example.dammpractice.domain.MemberRole;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtHelper {

	private static final int ACCESS_TOKEN_VALIDITY = 30 * 60 * 1000;
	private static final int REFRESH_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

	private static final String secretKey = "ch4njun";  // Vault, KMS(Key Management System) 저장소

	public static String generateAccessToken(Long memberId, MemberRole role) {
		Map<String, Object> claims = new HashMap<>();

		// "UN_CERTIFIED,USER,ADMIN"
		claims.put("authorities", role.getAuthority());
		// AccessToken 에서는 Claim 을 언제든지 꺼낼수 있다. -> 암호화 x

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(memberId.toString())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY))
			.signWith(SignatureAlgorithm.HS512, secretKey)
			.compact();
	}

	public static String generateRefreshToken(Long memberId) {
		return Jwts.builder()
			.setSubject(memberId.toString())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
			.signWith(SignatureAlgorithm.HS512, secretKey)
			.compact();
	}

	public static boolean validateJwt(String token) throws RuntimeException {
		return Jwts.parser().setSigningKey(secretKey).isSigned(token);
	}

	// Lambda 표현식
	// 함수형 인터페이스에 대해서 편하게
	// 함수 시그니처
	// R apply(T t);
	// (T) -> R
	// boolean test(T t);
	// (T) -> boolean
	// R apply(T t, U u);
	// (T, U) -> R
	public static <R> R getClaim(String token, Function<Claims, R> func) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
		// 인터페이스, 추상클래스 -> 객체
		// (T) -> R
		// (String) -> Long
		// Function<String, Long> f = (String s) -> Long.valueOf(s.length());
		return func.apply(claims);
	}

}
