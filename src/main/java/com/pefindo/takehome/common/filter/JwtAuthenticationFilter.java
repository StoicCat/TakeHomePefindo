package com.pefindo.takehome.common.filter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pefindo.takehome.service.impl.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtService jwtService;

	private static final List<String> PUBLIC_URLS = List.of("/api/v1/auth/register", "/api/v1/auth/login");

	private boolean isPublicUrl(HttpServletRequest request) {
		String path = request.getServletPath();
		return PUBLIC_URLS.stream().anyMatch(path::equalsIgnoreCase);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (isPublicUrl(request)) {
			filterChain.doFilter(request, response);
			return;
		}

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String token = authHeader.substring(7);

		if (jwtService.isTokenValid(token)) {
			UUID userId = jwtService.extractUserId(token);
			String email = jwtService.extractEmail(token);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, email,
					List.of());

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		filterChain.doFilter(request, response);
	}

}
