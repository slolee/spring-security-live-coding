package com.example.dammpractice.security.handler;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.dammpractice.security.exception.CustomAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest req, HttpServletResponse res, AuthenticationException failed) throws IOException, ServletException {

		/**
		 *  401 (UnAuthorization)
		 */
		CustomAuthenticationException exception = (CustomAuthenticationException) failed;

		res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);

		// Response Body -> failed.getMessage()
		// try - with - resource -> JAVA 8
		// Scanner sc = new Scanner(System.in);
		// try {
		// 	sc.nextLine();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// } finally {
		// 	if (sc != null) {
		// 		sc.close();
		// 	}
		// }
		try (OutputStream os = res.getOutputStream()) {
			new ObjectMapper().writeValue(os, exception.getMessage());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
