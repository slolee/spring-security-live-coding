package com.example.dammpractice.endpoint.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import com.example.dammpractice.domain.MemberType;
import com.example.dammpractice.endpoint.model.CertificateRequest;
import com.example.dammpractice.endpoint.model.MemberResponse;
import com.example.dammpractice.endpoint.model.RegisterRequest;
import com.example.dammpractice.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/api/members")
	public ResponseEntity<List<MemberResponse>> retrieveAllMember() {
		return ResponseEntity.ok(memberService.retrieveAllMember());
	}

	// 제가 아이디가 1, 제가 /api/member/2 -> 서비스 쪽에서
	@GetMapping("/api/members/{id}")
	public ResponseEntity<MemberResponse> retrieveMemberBy(@PathVariable Long id) {
		return ResponseEntity.ok(memberService.retrieveMemberBy(id));
	}

	@PostMapping("/api/certificate")
	public ResponseEntity<MemberResponse> certificate(@RequestBody CertificateRequest req) {
		return ResponseEntity.ok(memberService.certificate(req));
	}

	// Login
	// JWT -> 누가 인증한 건지 -> SecurityContext
	// ThreadLocal -> 해당 스레드에서 모두 접근할 수 있는 변수
	// Tomcat -> Request per Thread
	@GetMapping("/api/me")
	public ResponseEntity<MemberResponse> retrieveMe() {
		Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(memberService.retrieveMemberBy(memberId));
	}

	/**
	 *  결국 목적 : 권한(Role) 이 변경되고 AccessToken 을 새롭게 만들어줘야한다.
	 *   -> 권한정보가 변경된고 토큰 재발행 API 호출, 로그인 API ...
	 */
	@PreAuthorize("hasAuthority('CERTIFICATED')")
	@GetMapping("/api/post")
	public ResponseEntity<String> post() {
		return ResponseEntity.ok("게시글 작성 완료!");
	}

	@PostMapping("/register")
	public ResponseEntity<MemberResponse> register(@RequestBody RegisterRequest req) {
		return ResponseEntity.ok(memberService.register(req, MemberType.GENERAL));
	}

}
