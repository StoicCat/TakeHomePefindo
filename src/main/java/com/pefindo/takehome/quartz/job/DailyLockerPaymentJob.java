package com.pefindo.takehome.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.pefindo.takehome.quartz.service.DailyPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DailyLockerPaymentJob implements Job {
	private final DailyPaymentService DailyPaymentService;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("Starting daily locker payment processing...");
		try {
			DailyPaymentService.processDailyPayments();
			log.info("Daily locker payments processed successfully");
		} catch (Exception e) {
			log.error("Error during daily payment processing!", e);
		}
	}
}
