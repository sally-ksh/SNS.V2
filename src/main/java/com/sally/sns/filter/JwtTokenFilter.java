package com.sally.sns.filter;

import com.sally.sns.configuration.JwtConfiguration;
import com.sally.sns.model.User;
import com.sally.sns.service.UserService;
import com.sally.sns.util.JwtTokenUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
	private final UserService userService;
	private final JwtConfiguration jwtConfiguration;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
			log.error("Error of header, The header is null or invalid : {}", request.getRequestURL());
			filterChain.doFilter(request, response);
		}

		try {
			final String token = header.split(" ")[1].trim();
			if (JwtTokenUtils.isExpired(token, jwtConfiguration.getSecretKey())) {
				log.error("The token is expired");
				filterChain.doFilter(request, response);
				return;
			}
			String nickName = JwtTokenUtils.getUserName(token, jwtConfiguration.getSecretKey());
			User user = userService.loadUserByUserName(nickName);
			// check user valid
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user, null, List.of(new SimpleGrantedAuthority(user.getRole()))
			);
			authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
			securityContext.setAuthentication(authenticationToken);
		} catch (RuntimeException exception) {
			log.error("Error of accessor validation. {}", exception.getStackTrace());
			filterChain.doFilter(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}
}
