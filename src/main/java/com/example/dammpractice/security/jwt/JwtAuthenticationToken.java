package com.example.dammpractice.security.jwt;

import java.util.Collection;
import java.util.List;

import javax.transaction.NotSupportedException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.example.dammpractice.domain.MemberRole;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	public JwtAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	//  Collection<? extends GrantedAuthority> : 제네릭 와일드카드, 공변성
	public JwtAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public static JwtAuthenticationToken beforeOf(String accessToken) {
		return new JwtAuthenticationToken(accessToken, "");
	}

	public static JwtAuthenticationToken afterOf(long memberId, MemberRole role) {
		return new JwtAuthenticationToken(memberId, "", List.of(role));
	}

	public String getAccessToken() {
		return (String) this.getPrincipal();
	}

}
