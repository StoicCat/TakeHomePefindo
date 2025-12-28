package com.pefindo.takehome.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.pefindo.takehome.common.constant.LockerStatus;
import com.pefindo.takehome.model.response.LockerResponse;
import com.pefindo.takehome.persistence.entity.Locker;

public interface LockerService {
	List<LockerResponse> getAll(LockerStatus status);

	Optional<Locker> getEntityById(UUID id);

	void save(Locker entity);
}
