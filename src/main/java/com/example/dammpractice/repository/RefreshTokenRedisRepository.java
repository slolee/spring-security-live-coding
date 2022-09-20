package com.example.dammpractice.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.example.dammpractice.helper.JwtHelper;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRedisRepository {

	private final RedisTemplate<String, String> restTemplate;

	public void save(Long memberId, String refreshToken) {
		restTemplate.opsForValue().set(
			memberId.toString(),
			refreshToken,
			JwtHelper.getClaim(refreshToken, Claims::getExpiration).getTime(),
			TimeUnit.MILLISECONDS
		);
	}

	public String findBy(Long memberId) {
		return restTemplate.opsForValue().get(memberId.toString());
	}

	public void delete(Long memberId) {
		restTemplate.expire(memberId.toString(), 0, TimeUnit.SECONDS);
		// restTemplate.delete(memberId.toString());
	}

}
