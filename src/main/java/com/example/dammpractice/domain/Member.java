package com.example.dammpractice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Enumerated(EnumType.STRING)
	private MemberType type;

	@Builder
	public Member(String email, String password, String nickname, MemberRole role, MemberType type) {
		this.email = email;
		this.password = password;
		this.nickname = nickname;
		this.role = role;
		this.type = type;
	}

	public void certificate() {
		this.role = MemberRole.CERTIFICATED;
	}
}
