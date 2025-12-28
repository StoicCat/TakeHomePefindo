package com.pefindo.takehome.service.impl;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pefindo.takehome.persistence.entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	@Value("${security.jwt.secret}")
	private String SECRET_KEY;
	private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

	public String generateToken(User user) {
		return Jwts.builder().setSubject(user.getId().toString()).claim("email", user.getEmail())
				.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
	}

	public UUID extractUserId(String token) {
		return UUID.fromString(getClaims(token).getSubject());
	}

	public String extractEmail(String token) {
		return getClaims(token).get("email", String.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
	}

	public boolean isTokenValid(String token) {
		try {
			getClaims(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
	}

}
