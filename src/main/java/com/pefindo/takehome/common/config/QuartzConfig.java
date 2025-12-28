package com.pefindo.takehome.common.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.pefindo.takehome.quartz.job.DailyLockerPaymentJob;

@Configuration
public class QuartzConfig {
	@Bean
	public JobDetail dailyPaymentJobDetail() {
		return JobBuilder.newJob(DailyLockerPaymentJob.class)
			.withIdentity("dailyPaymentJob", "midnightJobs")
			.storeDurably()
			.build();
	}

	@Bean
	public Trigger dailyPaymentTrigger(JobDetail dailyPaymentJobDetail) {  
        return TriggerBuilder.newTrigger()
            .forJob(dailyPaymentJobDetail)                          
            .withIdentity("dailyLockerPaymentTrigger", "midnightJobs")
//            .withSchedule(CronScheduleBuilder.cronSchedule("0/15 * * * * ?"))
            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
            .build();
    }
}
