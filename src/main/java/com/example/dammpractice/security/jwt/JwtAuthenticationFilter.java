package com.example.dammpractice.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.example.dammpractice.security.exception.CustomAuthenticationException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String HEADER_PREFIX = "Bearer ";

	// matcher : URL -> 정규식    : /api/user/*  , /api/user/1  , /api/user/name/1
	public JwtAuthenticationFilter(RequestMatcher matcher) {
		super(matcher);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
		/**
		 *  인증에 필요한 정보 : AccessToken
		 *   -> Authorization Header : Bearer abcde... (JWT)
		 */
		String accessToken = req.getHeader("Authorization");
		if (accessToken == null) {
			throw new CustomAuthenticationException("Authorization Header Not Found!");
		}

		String accessTokenContent = accessToken.substring(HEADER_PREFIX.length());
		/**
		 *  실질적인 인증에 필요한 데이터 -> 인증 전 객체
		 */
		JwtAuthenticationToken beforeToken = JwtAuthenticationToken.beforeOf(accessTokenContent);
		return super.getAuthenticationManager().authenticate(beforeToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		JwtAuthenticationToken afterToken = (JwtAuthenticationToken) auth;

		/**
		 * 최종적으로 인증이 성공된 단계 -> Controller Handler
		 * 여기서 해야할 일은 SecurityContextHolder 에 인증된 사용자 정보를 넣어줘야함.
 		 */
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(afterToken);
		SecurityContextHolder.setContext(context);

		chain.doFilter(req, res);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		super.getFailureHandler().onAuthenticationFailure(req, res, failed);
	}

}
