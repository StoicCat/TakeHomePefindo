package com.pefindo.takehome.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pefindo.takehome.common.constant.LockerStatus;
import com.pefindo.takehome.model.response.ApiResponse;
import com.pefindo.takehome.model.response.LockerResponse;
import com.pefindo.takehome.service.LockerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/v1/lockers" })
@RequiredArgsConstructor
public class LockerController {
	private final LockerService service;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse<List<LockerResponse>>> getAll(
			@RequestParam(required = false) LockerStatus status) {
		List<LockerResponse> response = service.getAll(status);
		return ResponseEntity.ok(ApiResponse.<List<LockerResponse>>builder().success(true).data(response)
				.status(HttpStatus.OK.value()).build());
	}

}
