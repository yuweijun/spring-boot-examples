package com.example.spring.boot.service;

import org.springframework.remoting.RemoteAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

/**
 * @author yuweijun
 * @since 2018-11-19
 */
public interface RetryableService {

    @Retryable(value = {RemoteAccessException.class}, maxAttempts = 3, backoff = @Backoff(delay = 500l, multiplier = 1))
    int call(String arg);

    @Recover
    int recover(RemoteAccessException e, String arg);

}
