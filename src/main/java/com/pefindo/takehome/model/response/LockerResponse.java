package com.pefindo.takehome.model.response;

import java.util.UUID;

import com.pefindo.takehome.common.constant.LockerStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LockerResponse {
	private UUID id;
	private Integer lockerNo;
	private LockerStatus status;
}
