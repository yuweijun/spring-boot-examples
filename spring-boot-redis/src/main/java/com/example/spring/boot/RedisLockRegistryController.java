package com.example.spring.boot;

import com.google.common.base.Stopwatch;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @author yuweijun 2018-01-12.
 */
@RestController
@RequestMapping("/redis/lock/registry")
public class RedisLockRegistryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLockRegistryController.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @RequestMapping("/test3")
    public String test3() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Lock lock = redisLockRegistry.obtain("lock");

        LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        boolean b1 = lock.tryLock(5, TimeUnit.SECONDS);
        LOGGER.info("b1 is : {}, time : {}", b1, stopwatch.elapsed(TimeUnit.MILLISECONDS));

        TimeUnit.SECONDS.sleep(10);

        boolean b2 = lock.tryLock(5, TimeUnit.SECONDS);
        LOGGER.info("b2 is : {}, time : {}", b2, stopwatch.elapsed(TimeUnit.MILLISECONDS));

        LOGGER.info("======= test3 unlock");
        lock.unlock();
        LOGGER.info("======= test3 unlock");
        lock.unlock();

        return "test3";
    }

    @RequestMapping("/test4")
    public String test4() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.info("======= test4 get lock");
        Lock lock = redisLockRegistry.obtain("lock");
        try {
            LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
            lock.lock();
            LOGGER.info("======= test4 unlock");
        } finally {
            lock.unlock();
        }

        return "test4";
    }
}
