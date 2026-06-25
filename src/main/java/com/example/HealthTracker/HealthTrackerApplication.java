package com.example.HealthTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.Async;

@SpringBootApplication
@Async
@EnableCaching
public class HealthTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthTrackerApplication.class, args);
	}

}
