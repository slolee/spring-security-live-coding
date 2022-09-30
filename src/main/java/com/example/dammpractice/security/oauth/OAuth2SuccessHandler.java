package com.example.dammpractice.security.oauth;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.dammpractice.domain.Member;
import com.example.dammpractice.helper.JwtHelper;
import com.example.dammpractice.repository.MemberRepository;
import com.example.dammpractice.repository.RefreshTokenRedisRepository;
import com.example.dammpractice.service.MemberService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final MemberRepository memberRepository;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
		CustomOAuth2User user = (CustomOAuth2User) auth.getPrincipal();
		Long memberId = user.getMemberId();

		Member member = memberRepository.findById(memberId).orElseThrow();

		String accessToken = JwtHelper.generateAccessToken(member.getId(), member.getRole());
		String refreshToken = JwtHelper.generateRefreshToken(member.getId());
		refreshTokenRedisRepository.save(member.getId(), refreshToken);

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

}
