package com.example.dammpractice.security.handler;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.dammpractice.security.exception.CustomAuthenticationException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		/**
		 *  403 (FORBIDDEN)
		 */
		res.setStatus(HttpServletResponse.SC_FORBIDDEN);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);

		try (OutputStream os = res.getOutputStream()) {
			new ObjectMapper().writeValue(os, accessDeniedException.getMessage());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
