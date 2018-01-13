package com.example.spring.boot;

import com.google.common.base.Stopwatch;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yuweijun 2018-01-12.
 */
@RestController
@RequestMapping("/redisson/lock")
public class RedissonLockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonLockController.class);

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisLockRegistry redisLockRegistry;

    @RequestMapping("/test1")
    public String test1() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        RLock lock = redissonClient.getLock("lock");
        LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        lock.lock(15, TimeUnit.SECONDS);
        LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        Thread t = new Thread(() -> {
            RLock lock1 = redissonClient.getLock("lock");
            LOGGER.info("lock1 locked : {}", lock1.isLocked());
            LOGGER.info("lock1 is current thread : {}", lock1.isHeldByCurrentThread());
            if (lock1.isLocked()) {
                lock1.lock();
                LOGGER.info("======= test1 get lock1");
                LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
                LOGGER.info("lock is locked : {}", lock.isLocked());
                LOGGER.info("lock is current thread : {}", lock.isHeldByCurrentThread());
                lock1.unlock();
            } else {
                LOGGER.info("lock1 is not locked");
            }
        });
        t.start();
        t.join();
        LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        LOGGER.info("======= test1 unlock");
        lock.unlock();

        return "test1";
    }

    @RequestMapping("/test2")
    public String test2() throws InterruptedException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.info("======= test2 get lock");
        RLock lock = redissonClient.getLock("lock");

        LOGGER.info("lock is locked : {}", lock.isLocked());
        LOGGER.info("lock is current thread : {}", lock.isHeldByCurrentThread());
        LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        lock.lock();
        LOGGER.info("lock is locked : {}", lock.isLocked());
        LOGGER.info("lock is current thread : {}", lock.isHeldByCurrentThread());
        LOGGER.info("time : {}", stopwatch.elapsed(TimeUnit.MILLISECONDS));
        LOGGER.info("======= test2 unlock");
        lock.unlock();

        ExecutorService executor = Executors.newFixedThreadPool(10);
        AtomicInteger counter = new AtomicInteger(1);
        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 5; j++) {
                    // lock.lock(2, TimeUnit.SECONDS);
                    lock.lock();
                    try {
                        int nextValue = counter.get() + 1;
                        LOGGER.info("counter is : {}", counter);
                        Thread.sleep(1000);
                        counter.set(nextValue);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }

        executor.shutdown();

        return "test2";
    }

}
