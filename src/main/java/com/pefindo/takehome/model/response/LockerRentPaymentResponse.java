package com.pefindo.takehome.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.pefindo.takehome.common.constant.LockerRentPaymentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockerRentPaymentResponse {
	private UUID id;
	private UUID lockerRentId;
	private LockerRentPaymentType type;
	private BigDecimal amount;
	private LocalDateTime paymentDate;
}
