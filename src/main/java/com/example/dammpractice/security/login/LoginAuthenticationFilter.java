package com.example.dammpractice.security.login;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.example.dammpractice.endpoint.model.LoginRequest;
import com.example.dammpractice.helper.JwtHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;

public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public LoginAuthenticationFilter(String defaultUrl) {
		super(defaultUrl);
	}

	// req -> Request Body -> ID/Password -> Authentication Object
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
		LoginRequest loginReq = new ObjectMapper().readValue(req.getReader(), LoginRequest.class);
		LoginAuthenticationToken beforeToken = LoginAuthenticationToken.beforeOf(loginReq);
		return super.getAuthenticationManager().authenticate(beforeToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) throws IOException, ServletException {
		// Login 에 성공했으면 뭘할건지?
		// 1. AccessToken 만들기 (JWT)
		// 2. RefreshToken 만들기 (JWT)
		// 3. RefreshToken 을 쿠키에 담기  // Vue(Vuex), React(Redux) -> HttpOnly,,,
		// 4. RefreshToken 저장
		// 5. AccessToken 을 Response Body 에 담기

		LoginAuthenticationToken afterToken = (LoginAuthenticationToken) auth;
		String accessToken = afterToken.getAccessToken();
		String refreshToken = afterToken.getRefreshToken();

		Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		Date expiration = JwtHelper.getClaim(refreshToken, Claims::getExpiration);
		int maxAge = (int) ((expiration.getTime() - new Date(System.currentTimeMillis()).getTime()) / 1000);
		refreshTokenCookie.setMaxAge(maxAge);

		res.addCookie(refreshTokenCookie);
		res.getWriter().println(accessToken);

		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {
		// Login 에 실패했다면 뭘할건지?
	}

}
