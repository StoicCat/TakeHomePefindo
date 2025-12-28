package com.pefindo.takehome.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pefindo.takehome.common.constant.LockerRentPaymentType;
import com.pefindo.takehome.model.response.ApiResponse;
import com.pefindo.takehome.model.response.LockerRentPaymentResponse;
import com.pefindo.takehome.service.LockerRentPaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/v1/locker-rents" })
@RequiredArgsConstructor
public class LockerRentPaymentController {
	private final LockerRentPaymentService service;

	@GetMapping(
			value = "/{lockerRentId}/payments", 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<List<LockerRentPaymentResponse>>> getAllByLockerRentId(
			@PathVariable UUID lockerRentId,
			@RequestParam(required = false) LockerRentPaymentType type) {
		List<LockerRentPaymentResponse> response = service.getAllByLockerRentIdAndType(lockerRentId, type);
		return ResponseEntity.ok(ApiResponse.<List<LockerRentPaymentResponse>>builder().success(true).data(response)
				.status(HttpStatus.OK.value()).build());
	}
}
