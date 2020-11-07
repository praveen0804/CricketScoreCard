package com.cricket.score.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrixDashboard
@ComponentScan(basePackages = "com.cricket.score")
public class CricketScoreBoardApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(CricketScoreBoardApplication.class, args);
	}

}
