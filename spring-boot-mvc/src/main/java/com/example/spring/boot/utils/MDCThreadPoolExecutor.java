package com.example.spring.boot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yuweijun 2017-08-21.
 */
public class MDCThreadPoolExecutor extends ThreadPoolExecutor {

    public MDCThreadPoolExecutor(int corePoolSize,
                                 int maximumPoolSize,
                                 long keepAliveTime,
                                 TimeUnit unit,
                                 BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public static MDCThreadPoolExecutor getMDCThreadPoolExecutor(int corePoolSize,
                                                                 int maximumPoolSize,
                                                                 int queueSize) {
        return new MDCThreadPoolExecutor(corePoolSize, maximumPoolSize, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize));
    }

    @Override
    public void execute(Runnable command) {
        super.execute(mdc(command));
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MDCThreadPoolExecutor.class);

    /**
     * logback.xml
     *
     * <property name="CONSOLE_LOG_PATTERN" value="%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight(-%5p) --- [%15.15thread] %cyan(%-40.40logger{39} [%5line] :) %X{session} %m%n%ex"/>
     *
     */
    public static void main(String[] args) throws InterruptedException {
        MDCThreadPoolExecutor executor = getMDCThreadPoolExecutor(2, 5, 10);
        for (int i = 1; i <= 10; i++) {
            MDC.put("session", "session-" + i);
            executor.execute(() -> {
                Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
                LOGGER.info("MDC context is : {}", copyOfContextMap);
            });
        }
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }
}

