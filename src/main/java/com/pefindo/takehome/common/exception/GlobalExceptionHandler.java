package com.pefindo.takehome.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.pefindo.takehome.model.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(WebResponseException.class)
    public ResponseEntity<ApiResponse<Void>> handleWebResponse(WebResponseException ex) {
        return ResponseEntity
            .status(ex.getStatus())
            .body(ApiResponse.<Void>builder()
                .success(false)
                .data(null)
                .message(ex.getMessage())
                .status(ex.getStatus().value())
                .build()
            );
    }

	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.<Void>builder()
                .success(false)
                .data(null)
                .message("An unexpected error occurred")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build()
            );
    }
}
