package com.example.dammpractice.security.login;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.dammpractice.domain.Member;
import com.example.dammpractice.helper.JwtHelper;
import com.example.dammpractice.repository.RefreshTokenRedisRepository;
import com.example.dammpractice.security.exception.CustomAuthenticationException;
import com.example.dammpractice.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component // ComponentScan -> Bean
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {

	/**
	 * Provider : 실제로 인증 로직을 처리하는 곳
	 *  -> ID/PW 를 Database 에 있는 정보와 비교할 것이다.
	 */

	// DI (의존성 주입) : Bean 만 받을 수 있다.
	private final MemberService memberService;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		LoginAuthenticationToken beforeToken = (LoginAuthenticationToken) authentication;

		// beforeToken.getPrincipal() -> Email
		// beforeToken.getCredential() -> Password
		try {
			Member member = memberService.validate(beforeToken.getEmail(), beforeToken.getPassword());
			String accessToken = JwtHelper.generateAccessToken(member.getId(), member.getRole());
			String refreshToken = JwtHelper.generateRefreshToken(member.getId());
			refreshTokenRedisRepository.save(member.getId(), refreshToken);

			// accessToken, refreshToken -> 인증후 객체
			return LoginAuthenticationToken.afterOf(accessToken, refreshToken);
		} catch (RuntimeException e) {
			// 인증 실패에 대한 로직
			throw new CustomAuthenticationException("사용자 인증에 실패했습니다.", e);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// Reflection 라이브러리
		return LoginAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
