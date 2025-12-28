package com.pefindo.takehome.service.impl;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pefindo.takehome.common.constant.LockerRentLifeCycleStatus;
import com.pefindo.takehome.common.constant.LockerRentPaymentType;
import com.pefindo.takehome.common.constant.LockerRentUsageStatus;
import com.pefindo.takehome.common.constant.LockerStatus;
import com.pefindo.takehome.common.exception.WebResponseException;
import com.pefindo.takehome.common.utils.SecurityUtils;
import com.pefindo.takehome.model.request.CreateLockerRentRequest;
import com.pefindo.takehome.model.request.LockerRentPasswordRequest;
import com.pefindo.takehome.model.response.LockerRentResponse;
import com.pefindo.takehome.persistence.entity.Locker;
import com.pefindo.takehome.persistence.entity.LockerRent;
import com.pefindo.takehome.persistence.entity.User;
import com.pefindo.takehome.persistence.repository.LockerRentRepository;
import com.pefindo.takehome.service.EmailService;
import com.pefindo.takehome.service.LockerRentPaymentService;
import com.pefindo.takehome.service.LockerRentService;
import com.pefindo.takehome.service.LockerService;
import com.pefindo.takehome.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Service
@RequiredArgsConstructor
public class LockerRentServiceImpl implements LockerRentService {
	private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	private static final int LENGTH = 7;
	private static final SecureRandom random = new SecureRandom();
	private static final String EMAIL_LOCKER_BOOK_SUBJECT = "Locker Booking Complete";

	private final LockerRentRepository repository;
	private final LockerService lockerService;
	private final UserService userService;
	private final EmailService emailService;
	private final PasswordEncoder passwordEncoder;
	@Setter(onMethod_ = @Autowired, onParam_ = @Lazy)
	private LockerRentPaymentService paymentService;

	private void validateUserLocker(CreateLockerRentRequest request, User user) {
		Integer lockerCount = repository.countByUserIdAndLifeCycleStatus(user.getId(),
				LockerRentLifeCycleStatus.ACTIVE);
		if (lockerCount >= 3) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "User already have 3 active lockers");
		}
		if (lockerCount + request.getLockerIds().size() > 3) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST,
					"There too many lockers selected. User already have " + lockerCount + " lockers. (Max 3)");
		}
	}

	private void validateWrongPassword(LockerRent entity) {
		entity.setWrongPasswordCount(entity.getWrongPasswordCount() + 1);
		repository.save(entity);
		if (entity.getWrongPasswordCount() == 3) {
			entity.setUsageStatus(LockerRentUsageStatus.DONE);
			entity.setLifeCycleStatus(LockerRentLifeCycleStatus.BLOCKED);
			entity.setEndDate(LocalDateTime.now());
			repository.save(entity);

			Locker locker = entity.getLocker();
			locker.setStatus(LockerStatus.AVAILABLE);
			lockerService.save(locker);
			paymentService.createLockerPaymentWithPayment(entity, LockerRentPaymentType.WRONG_PASSWORD_PENALTY);
			throw new WebResponseException(HttpStatus.BAD_REQUEST,
					"You have exceeded total wrong password attempt! "
							+ "Now your locker will be blocked, and you will be fined with Rp.25.000. "
							+ "All of your deposit cannot be refunded as well");
		} else {
			repository.save(entity);

			throw new WebResponseException(HttpStatus.BAD_REQUEST,
					"locker password is incorrect! Total Attempt : " + entity.getWrongPasswordCount());
		}
	}

	@Override
	@Transactional
	public void rentLocker(CreateLockerRentRequest request) {
		int c = 0;
		User user = userService.getEntityById(SecurityUtils.getCurrentUserId())
				.orElseThrow(() -> new WebResponseException(HttpStatus.BAD_REQUEST, "User is not exist"));
		Map<Integer, String> userLocker = new HashMap();
		validateUserLocker(request, user);
		for (UUID lockerId : request.getLockerIds()) {
			if (Boolean.TRUE.equals(repository.existsByUserIdAndLockerIdAndLifeCycleStatus(user.getId(), lockerId,
					LockerRentLifeCycleStatus.ACTIVE))) {
				throw new WebResponseException(HttpStatus.BAD_REQUEST, "You already rented this locker");
			}
			Optional<Locker> optLocker = lockerService.getEntityById(lockerId);
			if (optLocker.isEmpty()) {
				throw new WebResponseException(HttpStatus.BAD_REQUEST, "Locker[" + c + "] Is Not Exists");
			}
			Locker locker = optLocker.get();
			if (!LockerStatus.AVAILABLE.equals(locker.getStatus())) {
				throw new WebResponseException(HttpStatus.BAD_REQUEST, "This locker is not available for rent");
			}
			locker.setStatus(LockerStatus.RENTED);
			lockerService.save(locker);
			String password = generateRandomPassword();
			userLocker.put(locker.getLockerNo(), password);
			LockerRent entity = mapToEntity(locker, user, password);

			repository.save(entity);
			paymentService.createLockerPaymentWithPayment(entity, LockerRentPaymentType.DAILY_DEPOSIT);
			c++;
		}
		sendEmail(user, userLocker);
	}

	private void sendEmail(User user, Map<Integer, String> userLocker) {
		StringBuilder emailBody = new StringBuilder();
		emailBody.append("This is your booked locker information").append("\n\n");
		userLocker.forEach((key, value) -> {
			emailBody.append("Locker Number : " + key + "\n");
			emailBody.append("Password : " + value + "\n\n");
		});
		emailBody.append("Please don't share this information to anyone!");
		emailService.sendSimpleEmail(user.getEmail(), EMAIL_LOCKER_BOOK_SUBJECT, emailBody.toString());
	}

	@Override
	@Transactional(dontRollbackOn = WebResponseException.class)
	public void openLocker(UUID id, LockerRentPasswordRequest request) {
		LockerRent entity = repository.findById(id)
				.orElseThrow(() -> new WebResponseException(HttpStatus.BAD_REQUEST, "locker is not exists"));
		if (!LockerRentUsageStatus.WAITING_STORE.equals(entity.getUsageStatus())) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "This locker cannot be open!");
		}
		if (!passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
			validateWrongPassword(entity);
			return;
		}
		entity.setPasswordUsedCount(entity.getPasswordUsedCount() + 1);
		entity.setUsageStatus(LockerRentUsageStatus.WAITING_RETRIEVE);

		repository.save(entity);

		Locker locker = entity.getLocker();
		locker.setStatus(LockerStatus.AVAILABLE);
		lockerService.save(locker);
	}

	@Override
	@Transactional(dontRollbackOn = WebResponseException.class)
	public void closeLocker(UUID id, LockerRentPasswordRequest request) {
		LockerRent entity = repository.findById(id)
				.orElseThrow(() -> new WebResponseException(HttpStatus.BAD_REQUEST, "locker is not exists"));
		if (!LockerRentUsageStatus.WAITING_RETRIEVE.equals(entity.getUsageStatus())) {
			throw new WebResponseException(HttpStatus.BAD_REQUEST, "This locker cannot be close!");
		}
		if (!passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
			validateWrongPassword(entity);
		}
		entity.setPasswordUsedCount(entity.getPasswordUsedCount() + 1);
		entity.setUsageStatus(LockerRentUsageStatus.DONE);
		entity.setLifeCycleStatus(LockerRentLifeCycleStatus.COMPLETED);
		entity.setEndDate(LocalDateTime.now());

		repository.save(entity);

		Locker locker = entity.getLocker();
		locker.setStatus(LockerStatus.AVAILABLE);
		lockerService.save(locker);
		paymentService.createLockerPaymentWithPayment(entity, LockerRentPaymentType.REFUND);
	}

	@Override
	public List<LockerRent> getAllEntityByLifeCycleStatus(LockerRentLifeCycleStatus lifeCycleStatus) {
		return repository.findAllByLifeCycleStatus(lifeCycleStatus);
	}

	@Override
	public List<LockerRentResponse> getAll(LockerRentLifeCycleStatus lifeCycleStatus,
			LockerRentUsageStatus usageStatus) {
		List<LockerRent> entities = new ArrayList<>();
		if (lifeCycleStatus != null && usageStatus != null) {
			entities = repository.findAllByUserIdAndUsageStatusAndLifeCycleStatus(SecurityUtils.getCurrentUserId(),
					usageStatus, lifeCycleStatus);
		} else if (lifeCycleStatus != null) {
			entities = repository.findAllByUserIdAndLifeCycleStatus(SecurityUtils.getCurrentUserId(), lifeCycleStatus);
		} else if (usageStatus != null) {
			entities = repository.findAllByUserIdAndUsageStatus(SecurityUtils.getCurrentUserId(), usageStatus);
		} else {
			entities = repository.findAllByUserId(SecurityUtils.getCurrentUserId());
		}

		return entities.stream().map(this::mapToResponse).toList();
	}

	private String generateRandomPassword() {
		StringBuilder sb = new StringBuilder(LENGTH);
		for (int i = 0; i < LENGTH; i++) {
			int index = random.nextInt(CHARACTERS.length());
			sb.append(CHARACTERS.charAt(index));
		}
		return sb.toString();
	}

	private LockerRent mapToEntity(Locker locker, User user, String password) {
		LockerRent entity = new LockerRent();
		entity.setLocker(locker);
		entity.setUser(user);
		entity.setPassword(passwordEncoder.encode(password));
		entity.setPasswordUsedCount(0);
		entity.setWrongPasswordCount(0);
		entity.setUsageStatus(LockerRentUsageStatus.WAITING_STORE);
		entity.setLifeCycleStatus(LockerRentLifeCycleStatus.ACTIVE);
		entity.setStartDate(LocalDateTime.now());
		entity.setEndDate(null);

		return entity;
	}

	private LockerRentResponse mapToResponse(LockerRent entity) {
		LockerRentResponse response = new LockerRentResponse();
		response.setId(entity.getId());
		response.setLockerId(entity.getLocker().getId());
		response.setUserId(entity.getUser().getId());
		response.setUsageStatus(entity.getUsageStatus());
		response.setLifeCycleStatus(entity.getLifeCycleStatus());
		response.setStartDate(entity.getStartDate());
		response.setEndDate(entity.getEndDate());
		response.setLockerNumber(entity.getLocker().getLockerNo());
		response.setWrongPasswordCount(entity.getWrongPasswordCount());

		return response;
	}

}
