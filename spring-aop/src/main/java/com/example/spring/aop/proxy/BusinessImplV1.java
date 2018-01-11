package com.example.spring.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yuweijun on 2018-01-11.
 */
public class BusinessImplV1 implements BusinessInterface {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessImplV1.class);

    @Override
    public void doSomething() {
        LOGGER.info(this.getClass().getCanonicalName());
    }

}
