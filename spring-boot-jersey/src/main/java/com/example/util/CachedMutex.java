package com.example.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuweijun on 2017-08-18.
 */
public final class CachedMutex {

    private static Object SHARE_OBJECT = new Object();

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedMutex.class);

    private static LoadingCache<String, Object> MUTEX = CacheBuilder.newBuilder().maximumSize(100_000)
            .expireAfterWrite(3600, TimeUnit.SECONDS).build(new CacheLoader<String, Object>() {
                public Object load(String key) throws Exception {
                    LOGGER.info("create tmux object for key : {}", key);
                    return new Object();
                }
            });

    /**
     * 根据key获取对象锁，用以控制并发
     *
     * @param key 可以是sessionId，也可以是customerId
     * @return 对象锁
     */
    public static Object getMutex(String key) {
        try {
            return MUTEX.get(key);
        } catch (ExecutionException e) {
            LOGGER.error("get mutex error and return default mutex object");
            return SHARE_OBJECT;
        }
    }

}
