package com.pefindo.takehome.model.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentUsageStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockerRentResponse {
	private UUID id;
	private UUID lockerId;
	private UUID userId;
	private Integer lockerNumber;
	private LockerRentUsageStatus usageStatus;
	private LockerRentLifeCycleStatus lifeCycleStatus;
	private Integer wrongPasswordCount;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
}
