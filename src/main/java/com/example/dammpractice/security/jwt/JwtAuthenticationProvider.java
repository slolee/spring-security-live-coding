package com.example.dammpractice.security.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.example.dammpractice.domain.MemberRole;
import com.example.dammpractice.helper.JwtHelper;
import com.example.dammpractice.security.exception.CustomAuthenticationException;

import io.jsonwebtoken.Claims;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		JwtAuthenticationToken beforeToken = (JwtAuthenticationToken) authentication;
		String accessToken = beforeToken.getAccessToken(); // JWT ->

		/**
		 *  accessToken -> 검증, 확인 ...
		 *  그 결과를 가지고 인증 후 객체를 만들어 반환
		 */
		try {
			if(!JwtHelper.validateJwt(accessToken)) {
				throw new CustomAuthenticationException("Invalid AccessToken!");
			}
			/**
			 *  토큰 검증 완료
			 *  인증 후 객체 만들어서 반환하기..!
			 *  Member 클래스
			 */
			long memberId = Long.parseLong(JwtHelper.getClaim(accessToken, Claims::getSubject));
			String role = (String) JwtHelper.getClaim(accessToken, claims -> claims.get("authorities"));
			MemberRole parseRole = MemberRole.valueOf(role);

			return JwtAuthenticationToken.afterOf(memberId, parseRole);
		} catch (RuntimeException ex) {
			throw new CustomAuthenticationException("Invalid AccessToken!", ex);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
