package com.example.spring.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;


/**
 * Created by yuweijun on 2018-01-15.
 */
public class BeforeAdviceExample implements MethodBeforeAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeforeAdviceExample.class);

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        LOGGER.info("method name : {}, and args : {} for target : {}", method.getName(), args, target);
    }

}
