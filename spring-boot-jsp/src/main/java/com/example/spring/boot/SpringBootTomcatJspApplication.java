package com.example.spring.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
public class SpringBootTomcatJspApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootTomcatJspApplication.class).run(args);
    }

}
