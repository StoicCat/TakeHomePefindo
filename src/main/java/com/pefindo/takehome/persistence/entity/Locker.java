package com.pefindo.takehome.persistence.entity;

import com.pefindo.takehome.common.constant.LockerStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lockers", uniqueConstraints = { @UniqueConstraint(columnNames = { "locker_no" }) })
@Getter
@Setter
@NoArgsConstructor
public class Locker extends AuditableEntity {

	@Column(name = "locker_no", nullable = false)
	private Integer lockerNo;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private LockerStatus status;

}
