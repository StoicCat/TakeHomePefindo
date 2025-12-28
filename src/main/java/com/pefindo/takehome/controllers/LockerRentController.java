package com.pefindo.takehome.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentUsageStatus;
import com.pefindo.takehome.model.request.CreateLockerRentRequest;
import com.pefindo.takehome.model.request.LockerRentPasswordRequest;
import com.pefindo.takehome.model.response.ApiResponse;
import com.pefindo.takehome.model.response.LockerRentResponse;
import com.pefindo.takehome.service.LockerRentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/v1/locker-rents" })
@RequiredArgsConstructor
public class LockerRentController {
	private final LockerRentService service;

	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<String>> rentLocker(@RequestBody @Valid CreateLockerRentRequest request) {
		service.rentLocker(request);
		return ResponseEntity.ok(ApiResponse.<String>builder().success(true)
				.message("Lockers booked. Password sent to email.").status(HttpStatus.OK.value()).build());
	}

	@PostMapping(
			value = "/{id}/open", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<String>> openLocker(@PathVariable UUID id,
			@RequestBody @Valid LockerRentPasswordRequest password) {
		service.openLocker(id, password);
		return ResponseEntity.ok(ApiResponse.<String>builder().success(true).message("Locker opened successfully")
				.status(HttpStatus.OK.value()).build());
	}
	
	@PostMapping(
			value = "/{id}/close", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<String>> closeLocker(@PathVariable UUID id,
			@RequestBody @Valid LockerRentPasswordRequest password) {
		service.closeLocker(id, password);
		return ResponseEntity.ok(ApiResponse.<String>builder().success(true).message("Locker closed successfully")
				.status(HttpStatus.OK.value()).build());
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<List<LockerRentResponse>>> getAll(
			@RequestParam(required = false) LockerRentLifeCycleStatus lifeCycleStatus,
			@RequestParam(required = false) LockerRentUsageStatus usageStatus) {
		return ResponseEntity.ok(ApiResponse.<List<LockerRentResponse>>builder().success(true)
				.data(service.getAll(lifeCycleStatus, usageStatus)).status(HttpStatus.OK.value()).build());
	}
	
}
