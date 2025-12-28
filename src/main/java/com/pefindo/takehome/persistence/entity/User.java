package com.pefindo.takehome.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "phone_number" }),
		@UniqueConstraint(columnNames = { "ktp_number" }), @UniqueConstraint(columnNames = { "email" }) })
@Getter
@Setter
@NoArgsConstructor
public class User extends AuditableEntity {

	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@Column(name = "ktp_number", nullable = false)
	private String ktpNumber;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;
}
