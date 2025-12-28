package com.pefindo.takehome.common.utils;

import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
	public static UUID getCurrentUserId() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return (UUID) principal;
	}

	public static String getCurrentUserEmail() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		return (String) principal;
	}
}
