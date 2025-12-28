package com.pefindo.takehome.service;

import java.util.Optional;
import java.util.UUID;

import com.pefindo.takehome.model.request.UserLoginRequest;
import com.pefindo.takehome.model.request.UserRegisterRequest;
import com.pefindo.takehome.model.response.LoginResponse;
import com.pefindo.takehome.persistence.entity.User;

public interface UserService {
	void register(UserRegisterRequest request);

	LoginResponse login(UserLoginRequest request);

	Optional<User> getEntityById(UUID id);
}
