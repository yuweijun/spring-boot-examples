package com.example.spring.jdbc.util;

import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * for spring task executor
 *
 * @author yuweijun 2017-08-22
 */
public class MDCThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    public static MDCThreadPoolTaskExecutor newFixedTaskExecutor(int poolSize, int queueCapacity) {
        MDCThreadPoolTaskExecutor taskExecutor = new MDCThreadPoolTaskExecutor();
        taskExecutor.setThreadFactory(new MDCThreadFactory());
        taskExecutor.setCorePoolSize(poolSize);
        taskExecutor.setMaxPoolSize(poolSize);
        taskExecutor.setQueueCapacity(queueCapacity);
        return taskExecutor;
    }

    static Runnable mdc(final Runnable runnable) {
        final Map<String, String> context = MDC.getCopyOfContextMap();
        return new Runnable() {
            @Override
            public void run() {
                Map<String, String> previous = MDC.getCopyOfContextMap();
                if (context == null) {
                    MDC.clear();
                } else {
                    MDC.setContextMap(context);
                }
                try {
                    runnable.run();
                } finally {
                    if (previous == null) {
                        MDC.clear();
                    } else {
                        MDC.setContextMap(previous);
                    }
                }
            }
        };
    }

    @Override
    public void execute(Runnable command) {
        super.execute(mdc(command));
    }

    static class MDCThreadFactory implements ThreadFactory {
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("mdc-thread-" + threadNumber.getAndIncrement());
            return thread;
        }
    }

}
