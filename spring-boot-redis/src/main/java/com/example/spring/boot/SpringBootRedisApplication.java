package com.example.spring.boot;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.integration.redis.util.RedisLockRegistry;

import java.util.UUID;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
public class SpringBootRedisApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootRedisApplication.class).run(args);
    }

    @Bean
    public RedisLockRegistry redisLockRegistry(RedisConnectionFactory redisConnectionFactory) {
        String registryKey = UUID.randomUUID().toString();
        RedisLockRegistry redisLockRegistry = new RedisLockRegistry(redisConnectionFactory, registryKey);
        return redisLockRegistry;
    }

    @Bean
    public RedissonClient redissonClient(Environment environment) {
        String host = environment.getProperty("spring.redis.host");
        Config config = new Config();
        String address = "redis://" + host + ":6379";
        config.useSingleServer().setAddress(address)
                .setDatabase(10)
                .setTimeout(600)
                .setRetryAttempts(2)
                .setReconnectionTimeout(3000);
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }

}
