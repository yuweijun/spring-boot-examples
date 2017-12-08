package com.example.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserServiceTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Test
	public void test() {
		String admin = new BCryptPasswordEncoder().encode("admin");
		logger.info(admin);
		String test = new BCryptPasswordEncoder().encode("test");
		logger.info(test);
	}

}
