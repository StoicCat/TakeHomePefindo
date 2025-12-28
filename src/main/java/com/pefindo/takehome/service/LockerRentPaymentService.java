package com.pefindo.takehome.service;

import java.util.List;
import java.util.UUID;

import com.pefindo.takehome.common.constant.LockerRentPaymentType;
import com.pefindo.takehome.model.response.LockerRentPaymentResponse;
import com.pefindo.takehome.persistence.entity.LockerRent;

public interface LockerRentPaymentService {
	List<LockerRentPaymentResponse> getAllByLockerRentIdAndType(UUID lockerRentId, LockerRentPaymentType type);

	void createLockerPaymentWithPayment(LockerRent lockerRent, LockerRentPaymentType type);
}
