package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * 
 * same as @Configuration @EnableAutoConfiguration @ComponentScan
 *
 */
@SpringBootApplication
public class SpringBootJerseyApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootJerseyApplication.class);

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(SpringBootJerseyApplication.class, args);
		String[] beanNames = ctx.getBeanDefinitionNames();
		LOGGER.info("bean sizes is: " + beanNames.length);
	}

}