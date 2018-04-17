package com.example.spring.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Service;

@Service
public class MyMetricsService {

	private final CounterService counterService;

	@Autowired
	public MyMetricsService(CounterService counterService) {
		this.counterService = counterService;
	}

	public void exampleMethod() {
		this.counterService.increment("services.system.mymetricsservice.invoked");
	}

}
