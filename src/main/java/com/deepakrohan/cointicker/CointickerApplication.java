package com.deepakrohan.cointicker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@EnableScheduling
@EnableAsync
@SpringBootApplication
public class CointickerApplication {

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job job;

	public static void main(String[] args) {
		SpringApplication.run(CointickerApplication.class, args);
	}

	@Scheduled(cron = "0 */1 * * * ?")
	public void run() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("JobId", String.valueOf(System.currentTimeMillis()))
				.toJobParameters();
		jobLauncher.run(job, jobParameters);
	}



}
