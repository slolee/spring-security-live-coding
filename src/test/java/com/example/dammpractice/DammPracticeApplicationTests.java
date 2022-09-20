package com.example.dammpractice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.dammpractice.helper.JwtHelper;

class DammPracticeApplicationTests {

	@Test
	void contextLoads() {
		String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjYzNTk2NzI5LCJpYXQiOjE2NjM1OTQ5MjksImF1dGhv"
			+ "cml0aWVzIjoiVU5fQ0VSVElGSUNBVEVEIn0.xzfY5VRbOpJ9i0YoIt2Czo6VeaC9Egdzs0TCAQRV3YrYyVuRqpgAmNhHIvN0o5xO33HU"
			+ "_yM7TOsqtLKTiMn9w";

		System.out.println(JwtHelper.validateJwt(accessToken));
	}

}
