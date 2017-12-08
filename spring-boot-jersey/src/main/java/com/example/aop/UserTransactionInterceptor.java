package com.example.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserTransactionInterceptor {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Pointcut("execution(public * com.example.service..*.create(..))")
	public void userSave() {
	}

	@After("userSave()")
	public void after() {
		logger.info("user create after ==============");
	}

	@Before("userSave()")
	public void before() {
		logger.info("user create before ================");
	}

}
