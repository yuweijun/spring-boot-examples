package com.example.spring.boot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.remoting.RemoteAccessException;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author yuweijun
 * @since 2018-11-19
 */
@Service
public class RetryableServiceImpl implements RetryableService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryableServiceImpl.class);

    private static LongAdder counter = new LongAdder();

    // @Retryable(value = {RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000l, multiplier = 1))
    @Override
    public int call(String arg) {
        counter.increment();
        LOGGER.info("arg : {} , counter : {}", arg, counter.longValue());
        throw new RemoteAccessException("exception ...");
    }

    // @Recover
    @Override
    public int recover(RemoteAccessException e, String arg) {
        LOGGER.info("recover arg : {} , counter : {}", arg, counter.longValue());
        LOGGER.error("exception received by recover of spring", e);
        return 0;
    }
}
