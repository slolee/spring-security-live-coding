package com.example.dammpractice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	private String email;
	private String password;
	private String nickname;

	@Enumerated(EnumType.STRING)
	private MemberRole role;

	@Builder
	public Member(String email, String password, String nickname, MemberRole role) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
	}

	public void certificate() {
		this.role = MemberRole.CERTIFICATED;
	}
}
