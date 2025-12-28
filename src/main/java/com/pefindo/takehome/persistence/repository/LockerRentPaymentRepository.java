package com.pefindo.takehome.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pefindo.takehome.common.constant.LockerRentPaymentType;
import com.pefindo.takehome.persistence.entity.LockerRentPayment;

public interface LockerRentPaymentRepository extends JpaRepository<LockerRentPayment, UUID> {
	List<LockerRentPayment> getAllByLockerRentId(UUID lockerRentId);

	List<LockerRentPayment> getAllByLockerRentIdAndType(UUID lockerRentId, LockerRentPaymentType type);
}
