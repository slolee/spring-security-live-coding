package com.example.dammpractice.security.login;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.example.dammpractice.endpoint.model.LoginRequest;

import lombok.AllArgsConstructor;
import lombok.Data;

public class LoginAuthenticationToken extends UsernamePasswordAuthenticationToken {

	/**
	 * 인증객체 (Authentication Object) -> 2가지 종류
	 *   -> 인증전 객체, 인증후 객체
	 */

	// 인증전 객체
	public LoginAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	// 인증후 객체
	public LoginAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public String getEmail() {
		return (String) this.getPrincipal();
	}

	public String getPassword() {
		return (String) this.getCredentials();
	}

	public String getAccessToken() {
		return ((JwtDto) this.getPrincipal()).getAccessToken();
	}

	public String getRefreshToken() {
		return ((JwtDto) this.getPrincipal()).getRefreshToken();
	}

	public static LoginAuthenticationToken beforeOf(LoginRequest loginReq) {
		return new LoginAuthenticationToken(loginReq.getEmail(), loginReq.getPassword());
	}

	public static LoginAuthenticationToken afterOf(String accessToken, String refreshToken) {
		JwtDto jwt = new JwtDto(accessToken, refreshToken);
		return new LoginAuthenticationToken(jwt, "", List.of());
	}

	@Data
	@AllArgsConstructor
	static class JwtDto {
		private String accessToken;
		private String refreshToken;
	}

}
