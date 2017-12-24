package com.example.spring.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
public class SpringBootRestApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootRestApplication.class).run(args);
    }

}
