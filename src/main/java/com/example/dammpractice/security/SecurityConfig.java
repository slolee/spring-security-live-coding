package com.example.dammpractice.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import com.example.dammpractice.security.handler.CustomAccessDeniedHandler;
import com.example.dammpractice.security.handler.CustomAuthenticationEntryPoint;
import com.example.dammpractice.security.jwt.JwtAuthenticationFilter;
import com.example.dammpractice.security.jwt.JwtAuthenticationProvider;
import com.example.dammpractice.security.login.LoginAuthenticationFilter;
import com.example.dammpractice.security.login.LoginAuthenticationProvider;
import com.example.dammpractice.security.oauth.CustomOauth2UserService;
import com.example.dammpractice.security.oauth.OAuth2SuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final List<String> JWT_PATHS = List.of("/api/**", "/api2/**");

	private final LoginAuthenticationProvider loginAuthenticationProvider;
	private final JwtAuthenticationProvider jwtAuthenticationProvider;
	private final CustomAuthenticationEntryPoint authenticationEntryPoint;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CustomOauth2UserService oauth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;

	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	public LoginAuthenticationFilter loginFiler() throws Exception {
		LoginAuthenticationFilter loginFilter = new LoginAuthenticationFilter("/login");
		loginFilter.setAuthenticationManager(super.authenticationManager());
		loginFilter.setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint));
		return loginFilter;
	}

	public JwtAuthenticationFilter jwtFilter() throws Exception {
		OrRequestMatcher matcher = new OrRequestMatcher(JWT_PATHS.stream()
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList()));
		JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(matcher);
		jwtFilter.setAuthenticationManager(super.authenticationManager());
		jwtFilter.setAuthenticationFailureHandler(new AuthenticationEntryPointFailureHandler(authenticationEntryPoint));
		return jwtFilter;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(loginAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
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
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)

			.and()
			.addFilterBefore(loginFiler(), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)

			.oauth2Login()
			.redirectionEndpoint().baseUri("/oauth2/login/callback/*")
			.and().userInfoEndpoint().userService(oauth2UserService)
			.and().successHandler(oAuth2SuccessHandler);
	}

}
