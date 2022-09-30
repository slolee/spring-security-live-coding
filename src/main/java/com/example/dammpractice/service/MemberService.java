package com.example.dammpractice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dammpractice.domain.Member;
import com.example.dammpractice.domain.MemberRole;
import com.example.dammpractice.domain.MemberType;
import com.example.dammpractice.endpoint.model.CertificateRequest;
import com.example.dammpractice.endpoint.model.MemberResponse;
import com.example.dammpractice.endpoint.model.RegisterRequest;
import com.example.dammpractice.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	// DI
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public MemberResponse register(RegisterRequest req, MemberType type) {
		Member newMember = Member.builder()
			.email(req.getEmail())
			.password(passwordEncoder.encode(req.getPassword()))
			.nickname(req.getNickname())
			.role(MemberRole.UN_CERTIFICATED)
			.type(type)
			.build();
		return MemberResponse.of(memberRepository.save(newMember));
	}

	public List<MemberResponse> retrieveAllMember() {
		return memberRepository.findAll().stream()
			.map(MemberResponse::of)
			.collect(Collectors.toList());
	}

	public MemberResponse retrieveMemberBy(Long id) {
		return memberRepository.findById(id)
			.map(MemberResponse::of)
			.orElseThrow(() -> new RuntimeException("Not Found Member!"));
	}

	public MemberResponse retrieveMemberByEmail(String email) {
		return memberRepository.findMemberByEmail(email)
			.map(MemberResponse::of)
			.orElseThrow(() -> new RuntimeException("Not Found Member!"));
	}

	// Checked Exception (Exception) & Unchecked Exception (RuntimeException)
	public Member validate(String email, String password) throws RuntimeException {
		return memberRepository.findMemberByEmail(email)
			.filter(member -> passwordEncoder.matches(password, member.getPassword()))
			.orElseThrow(() -> new RuntimeException("Validate X!"));
	}

	public MemberResponse certificate(CertificateRequest req) {
		return memberRepository.findById(req.getMemberId())
			.map(member -> {
				member.certificate();
				return memberRepository.save(member);
			})
			.map(MemberResponse::of)
			.orElseThrow(() -> new RuntimeException("Not Found Member!"));
	}

	public boolean existsByEmail(String email) {
		return memberRepository.existsByEmail(email);
	}
}
