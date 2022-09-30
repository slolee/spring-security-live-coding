package com.example.dammpractice.endpoint.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {

	private String email;
	private String password;  // TODO : HTTPS
	private String nickname;

}
