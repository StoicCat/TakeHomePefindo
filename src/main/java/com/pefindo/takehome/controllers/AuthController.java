package com.pefindo.takehome.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pefindo.takehome.model.request.UserLoginRequest;
import com.pefindo.takehome.model.request.UserRegisterRequest;
import com.pefindo.takehome.model.response.ApiResponse;
import com.pefindo.takehome.model.response.LoginResponse;
import com.pefindo.takehome.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService service;

	@PostMapping(
			value = "/register",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<String>> register(@RequestBody @Valid UserRegisterRequest request) {
		service.register(request);
		return ResponseEntity.ok(ApiResponse.<String>builder().success(true).message("User Account Register Success")
				.status(HttpStatus.OK.value()).build());
	}

	@PostMapping(
			value = "/login", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid UserLoginRequest request) {
		LoginResponse response = service.login(request);
		return ResponseEntity.ok(ApiResponse.<LoginResponse>builder().success(true).data(response)
				.message("Login Success").status(HttpStatus.OK.value()).build());
	}
}
