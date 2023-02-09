package com.sally.sns.configuration;

import com.sally.sns.exception.MyAuthenticationEntryPoint;
import com.sally.sns.filter.JwtTokenFilter;
import com.sally.sns.service.UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class AuthenticationConfiguration {
	private final UserService userService;
	private final JwtConfiguration jwtConfiguration;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.csrf().disable()
			.authorizeRequests()
			// .antMatchers("/api/*/users/join", "/api/*/users/login").permitAll()
			.antMatchers("/api/**").authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.exceptionHandling()
			.authenticationEntryPoint(new MyAuthenticationEntryPoint())
			.and()
			.addFilterBefore(new JwtTokenFilter(userService, jwtConfiguration),
				UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
			.antMatchers(HttpMethod.POST, "/api/*/users/join", "/api/*/users/login");
	}
}
