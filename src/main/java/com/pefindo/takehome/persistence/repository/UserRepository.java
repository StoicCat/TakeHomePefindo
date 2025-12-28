package com.pefindo.takehome.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pefindo.takehome.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByEmail(String email);

	Boolean existsByEmail(String email);

	Boolean existsByKtpNumber(String ktpNumber);

	Boolean existsByPhoneNumber(String phoneNumber);
}
