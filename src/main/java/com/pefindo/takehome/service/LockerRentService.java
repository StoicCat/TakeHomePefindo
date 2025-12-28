package com.pefindo.takehome.service;

import java.util.List;
import java.util.UUID;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentUsageStatus;
import com.pefindo.takehome.model.request.CreateLockerRentRequest;
import com.pefindo.takehome.model.request.LockerRentPasswordRequest;
import com.pefindo.takehome.model.response.LockerRentResponse;
import com.pefindo.takehome.persistence.entity.LockerRent;

public interface LockerRentService {
	void rentLocker(CreateLockerRentRequest request);

	void openLocker(UUID id, LockerRentPasswordRequest password);

	void closeLocker(UUID id, LockerRentPasswordRequest password);

	List<LockerRentResponse> getAll(LockerRentLifeCycleStatus lifeCycleStatus, LockerRentUsageStatus usageStatus);

	List<LockerRent> getAllEntityByLifeCycleStatus(LockerRentLifeCycleStatus lifeCycleStatus);
}
