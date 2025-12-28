package com.pefindo.takehome.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
	@NotBlank
	private String ktpNumber;
	@NotBlank
	private String phoneNumber;
	@Email
	private String email;
	@NotBlank
	private String password;
}
