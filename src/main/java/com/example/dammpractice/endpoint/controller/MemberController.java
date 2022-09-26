package com.example.dammpractice.endpoint.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

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

	// Login
	// JWT -> 누가 인증한 건지 -> SecurityContext
	// ThreadLocal -> 해당 스레드에서 모두 접근할 수 있는 변수
	// Tomcat -> Request per Thread
	@GetMapping("/api/me")
	public ResponseEntity<MemberResponse> retrieveMe() {
		Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return ResponseEntity.ok(memberService.retrieveMemberBy(memberId));
	}

	@PostMapping("/register")
	public ResponseEntity<MemberResponse> register(@RequestBody RegisterRequest req) {
		return ResponseEntity.ok(memberService.register(req));
	}

}
