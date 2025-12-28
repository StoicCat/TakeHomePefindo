package com.pefindo.takehome.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentUsageStatus;
import com.pefindo.takehome.persistence.entity.LockerRent;

public interface LockerRentRepository extends JpaRepository<LockerRent, UUID> {
	Integer countByUserIdAndLifeCycleStatus(UUID userId, LockerRentLifeCycleStatus status);

	List<LockerRent> findAllByUserId(UUID userId);

	List<LockerRent> findAllByUserIdAndUsageStatus(UUID userId, LockerRentUsageStatus usageStatus);

	List<LockerRent> findAllByUserIdAndLifeCycleStatus(UUID userId, LockerRentLifeCycleStatus lifeCycleStatus);

	List<LockerRent> findAllByUserIdAndUsageStatusAndLifeCycleStatus(UUID userId, LockerRentUsageStatus usageStatus,
			LockerRentLifeCycleStatus lifeCycleStatus);

	List<LockerRent> findAllByLifeCycleStatus(LockerRentLifeCycleStatus lifeCycleStatus);

	Boolean existsByUserIdAndLockerIdAndLifeCycleStatus(UUID userId, UUID lockerId,
			LockerRentLifeCycleStatus lifeCycleStatus);
}
