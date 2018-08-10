package com.example.spring.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yuweijun on 2018-08-10.
 */
@Service
public class HelloService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloService.class);

    @Autowired
    private RequestScopeExample requestScopeExample;

    public void print() {
        LOGGER.info("date is : [{}]", requestScopeExample.getDate());
    }

}
