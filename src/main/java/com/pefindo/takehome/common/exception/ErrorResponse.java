package com.pefindo.takehome.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResponse(String message, int status, String statusText) {
	public static ErrorResponse from(WebResponseException ex) {
		return new ErrorResponse(ex.getMessage(), ex.getStatus().value(), ex.getStatus().getReasonPhrase());
	}

	public static ErrorResponse unexpected() {
		return new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(),
				HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
	}
}
