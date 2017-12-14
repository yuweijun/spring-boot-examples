package com.example.spring.jsp;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Configuration;

/**
 * @author yuweijun 2017-12-07
 */
@Configuration
@EnableAutoConfigurationExample
public class SpringBootAutoConfigurationApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootAutoConfigurationApplication.class).run(args);
    }

}
