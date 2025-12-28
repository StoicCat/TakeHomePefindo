package com.pefindo.takehome.persistence.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pefindo.takehome.common.constant.LockerRentPaymentType;

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
@Table(name = "locker_rent_payments")
@Getter
@Setter
@NoArgsConstructor
public class LockerRentPayment extends AuditableEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "locker_rent_id", nullable = false)
	private LockerRent lockerRent;

	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private LockerRentPaymentType type;

	@Column(name = "amount", nullable = false)
	private BigDecimal amount;

	@Column(name = "payment_date", nullable = false)
	private LocalDateTime paymentDate;

}
