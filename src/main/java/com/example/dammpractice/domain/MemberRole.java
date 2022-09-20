package com.example.dammpractice.domain;

import org.springframework.security.core.GrantedAuthority;

public enum MemberRole implements GrantedAuthority {

	UN_CERTIFICATED, CERTIFICATED;

	@Override
	public String getAuthority() {
		// UN_CERTIFICATED
		return this.name();
	}

}

