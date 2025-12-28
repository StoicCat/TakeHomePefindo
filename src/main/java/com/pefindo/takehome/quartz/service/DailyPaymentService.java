package com.pefindo.takehome.quartz.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentPaymentType;
import com.pefindo.takehome.persistence.entity.LockerRent;
import com.pefindo.takehome.service.LockerRentPaymentService;
import com.pefindo.takehome.service.LockerRentService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DailyPaymentService {
	private final LockerRentService lockerRentService;
	private final LockerRentPaymentService lockerRentPaymentService;

	@Transactional
	public void processDailyPayments() {
		List<LockerRent> activeRentals = lockerRentService
				.getAllEntityByLifeCycleStatus(LockerRentLifeCycleStatus.ACTIVE);
		for (LockerRent lockerRent : activeRentals) {
			lockerRentPaymentService.createLockerPaymentWithPayment(lockerRent, LockerRentPaymentType.DAILY_DEPOSIT);
			lockerRentPaymentService.createLockerPaymentWithPayment(lockerRent, LockerRentPaymentType.DAILY_PENALTY);
		}
	}
}
