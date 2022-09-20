package com.example.dammpractice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.dammpractice.security.login.LoginAuthenticationFilter;
import com.example.dammpractice.security.login.LoginAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final LoginAuthenticationProvider loginAuthenticationProvider;

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	public LoginAuthenticationFilter loginFiler() throws Exception {
		LoginAuthenticationFilter loginFilter = new LoginAuthenticationFilter("/login");
		loginFilter.setAuthenticationManager(super.authenticationManager());
		return loginFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(loginAuthenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic().disable()
			.formLogin().disable()
			.cors().disable()
			.csrf().disable()
			.headers().frameOptions().disable()

			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

			.and()
			.addFilterBefore(loginFiler(), UsernamePasswordAuthenticationFilter.class);
	}

}
