package com.pefindo.takehome.persistence.entity;

import java.time.LocalDateTime;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentUsageStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locker_rents")
@Getter
@Setter
@NoArgsConstructor
public class LockerRent extends AuditableEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locker_id", nullable = false)
	private Locker locker;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "password_used_count", nullable = false)
	private Integer passwordUsedCount;

	@Column(name = "wrong_password_count", nullable = false)
	private Integer wrongPasswordCount;

	@Enumerated(EnumType.STRING)
	@Column(name = "usage_status", nullable = false)
	private LockerRentUsageStatus usageStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "lifecycle_status", nullable = false)
	private LockerRentLifeCycleStatus lifeCycleStatus;

	@Column(name = "rent_start_date", nullable = false)
	private LocalDateTime startDate;

	@Column(name = "rent_end_date", nullable = true)
	private LocalDateTime endDate;

}
