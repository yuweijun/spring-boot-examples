package com.example.spring.boot.sharding.jpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class SpringBootShardingJdbcApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootShardingJdbcApplication.class);

    public static void main(final String[] args) {
        new SpringApplicationBuilder(SpringBootShardingJdbcApplication.class).web(false).run(args);
        LOGGER.info("spring application with sharding jdbc started");
    }

}
