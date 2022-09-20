package com.example.dammpractice.endpoint.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	@GetMapping("/ping")
	public ResponseEntity<String> ping() {
		return ResponseEntity.ok("pong");
	}
	// DDD (Domain Driven Development)

}
