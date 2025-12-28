package com.pefindo.takehome.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pefindo.takehome.common.constant.LockerStatus;
import com.pefindo.takehome.persistence.entity.Locker;

public interface LockerRepository extends JpaRepository<Locker, UUID> {
	List<Locker> findAllByStatus(LockerStatus status);
}
