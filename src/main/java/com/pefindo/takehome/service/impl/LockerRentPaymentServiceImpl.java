package com.pefindo.takehome.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pefindo.takehome.common.constant.LockerRentPaymentType;
import com.pefindo.takehome.model.response.LockerRentPaymentResponse;
import com.pefindo.takehome.persistence.entity.LockerRent;
import com.pefindo.takehome.persistence.entity.LockerRentPayment;
import com.pefindo.takehome.persistence.repository.LockerRentPaymentRepository;
import com.pefindo.takehome.service.LockerRentPaymentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LockerRentPaymentServiceImpl implements LockerRentPaymentService {
	private final LockerRentPaymentRepository repository;

	@Override
	public List<LockerRentPaymentResponse> getAllByLockerRentIdAndType(UUID lockerRentId, LockerRentPaymentType type) {
		if (type != null) {
			return repository.getAllByLockerRentIdAndType(lockerRentId, type).stream().map(this::mapToResponse)
					.toList();
		} else {
			return repository.getAllByLockerRentId(lockerRentId).stream().map(this::mapToResponse).toList();
		}
	}

	private List<LockerRentPayment> getAllEntityByLockerRentIdAndType(UUID lockerRentId, LockerRentPaymentType type) {
		return repository.getAllByLockerRentIdAndType(lockerRentId, type);
	}

	@Override
	@Transactional
	public void createLockerPaymentWithPayment(LockerRent lockerRent, LockerRentPaymentType type) {
		LockerRentPayment entity = switch (type) {
		case DAILY_DEPOSIT -> mapToEntityWithType(lockerRent, type, BigDecimal.valueOf(10000));
		case REFUND -> mapToEntityWithType(lockerRent, type, getTotalRefundAmount(lockerRent));
		case DAILY_PENALTY -> mapToEntityWithType(lockerRent, type, BigDecimal.valueOf(5000));
		case WRONG_PASSWORD_PENALTY -> mapToEntityWithType(lockerRent, type, BigDecimal.valueOf(25000));
		default -> throw new IllegalArgumentException("Unsupported payment type: " + type);
		};

		repository.save(entity);
	}

	private BigDecimal getTotalRefundAmount(LockerRent lockerRent) {
		UUID lockerRentId = lockerRent.getId();
		BigDecimal totalDeposits = getAllEntityByLockerRentIdAndType(lockerRentId, LockerRentPaymentType.DAILY_DEPOSIT)
				.stream().map(LockerRentPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal totalPenalties = getAllEntityByLockerRentIdAndType(lockerRentId, LockerRentPaymentType.DAILY_PENALTY)
				.stream().map(LockerRentPayment::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		return totalDeposits.subtract(totalPenalties);
	}

	private LockerRentPayment mapToEntityWithType(LockerRent lockerRent, LockerRentPaymentType type, BigDecimal value) {
		LockerRentPayment entity = new LockerRentPayment();
		entity.setLockerRent(lockerRent);
		entity.setType(type);
		entity.setAmount(value);
		entity.setPaymentDate(LocalDateTime.now());

		return entity;
	}

	private LockerRentPaymentResponse mapToResponse(LockerRentPayment entity) {
		LockerRentPaymentResponse response = new LockerRentPaymentResponse();
		response.setId(entity.getId());
		response.setLockerRentId(entity.getLockerRent().getId());
		response.setType(entity.getType());
		response.setAmount(entity.getAmount());
		response.setPaymentDate(entity.getPaymentDate());

		return response;
	}

}
