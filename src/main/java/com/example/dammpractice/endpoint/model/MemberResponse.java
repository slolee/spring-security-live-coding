package com.example.dammpractice.endpoint.model;

import com.example.dammpractice.domain.Member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {

	private Long memberId;
	private String email;
	private String nickname;

	public static MemberResponse of(Member member) {
		return new MemberResponse(member.getId(), member.getEmail(), member.getNickname());
	}

}
