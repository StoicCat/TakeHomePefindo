package com.pefindo.takehome.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.pefindo.takehome.common.constant.LockerStatus;
import com.pefindo.takehome.model.response.LockerResponse;
import com.pefindo.takehome.persistence.entity.Locker;
import com.pefindo.takehome.persistence.repository.LockerRepository;
import com.pefindo.takehome.service.LockerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LockerServiceImpl implements LockerService {
	private final LockerRepository repository;

	@Override
	public List<LockerResponse> getAll(LockerStatus status) {
		if (status != null) {
			return repository.findAllByStatus(status).stream().map(this::mapToResponse).toList();
		} else {
			return repository.findAll().stream().map(this::mapToResponse).toList();
		}
	}

	@Override
	@Transactional
	public void save(Locker entity) {
		repository.save(entity);
	}

	@Override
	public Optional<Locker> getEntityById(UUID id) {
		return repository.findById(id);
	}

	private LockerResponse mapToResponse(Locker entity) {
		LockerResponse response = new LockerResponse();
		response.setId(entity.getId());
		response.setLockerNo(entity.getLockerNo());
		response.setStatus(entity.getStatus());

		return response;
	}

}
