package com.example.dammpractice.endpoint.model;

import lombok.Data;

@Data
public class RegisterRequest {

	private String email;
	private String password;  // TODO : HTTPS
	private String nickname;

}
