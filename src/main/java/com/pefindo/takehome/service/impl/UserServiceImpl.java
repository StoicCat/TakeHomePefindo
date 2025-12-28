package com.pefindo.takehome.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pefindo.takehome.common.exception.WebResponseException;
import com.pefindo.takehome.model.request.UserLoginRequest;
import com.pefindo.takehome.model.request.UserRegisterRequest;
import com.pefindo.takehome.model.response.LoginResponse;
import com.pefindo.takehome.persistence.entity.User;
import com.pefindo.takehome.persistence.repository.UserRepository;
import com.pefindo.takehome.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository repository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	private void validateRegister(UserRegisterRequest request) {
		if (repository.existsByEmail(request.getEmail())) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "Email Already Exists");
		}
		if (repository.existsByKtpNumber(request.getKtpNumber())) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "Ktp Number Already Exists");
		}
		if (repository.existsByPhoneNumber(request.getPhoneNumber())) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "Phone Number Already Exists");
		}
	}

	@Override
	@Transactional
	public void register(UserRegisterRequest request) {
		validateRegister(request);
		User entity = mapToUser(request);

		repository.save(entity);
	}

	@Override
	public LoginResponse login(UserLoginRequest request) {
		User user = repository.findByEmail(request.getEmail())
				.orElseThrow(() -> new WebResponseException(HttpStatus.BAD_REQUEST, "User not found"));
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "Invalid credentials");
		}

		String token = jwtService.generateToken(user);
		return new LoginResponse(token);
	}

	private User mapToUser(UserRegisterRequest request) {
		User entity = new User();
		entity.setPhoneNumber(request.getPhoneNumber());
		entity.setEmail(request.getEmail());
		entity.setKtpNumber(request.getKtpNumber());
		entity.setPassword(passwordEncoder.encode(request.getPassword()));

		return entity;
	}

	@Override
	public Optional<User> getEntityById(UUID id) {
		return repository.findById(id);
	}

}
