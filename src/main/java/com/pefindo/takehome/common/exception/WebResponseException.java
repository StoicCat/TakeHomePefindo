package com.pefindo.takehome.common.exception;

import org.springframework.http.HttpStatus;

public class WebResponseException extends RuntimeException {
	private final HttpStatus status;

	public WebResponseException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public WebResponseException(HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public static WebResponseException notFound(String message) {
		return new WebResponseException(HttpStatus.NOT_FOUND, message);
	}

	public static WebResponseException badRequest(String message) {
		return new WebResponseException(HttpStatus.BAD_REQUEST, message);
	}

	public static WebResponseException internalServerError(String message) {
		return new WebResponseException(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

	public HttpStatus getStatus() {
		return status;
	}
}
