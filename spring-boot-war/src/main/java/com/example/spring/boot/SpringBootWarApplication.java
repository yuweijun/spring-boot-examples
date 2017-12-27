package com.example.spring.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
public class SpringBootWarApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootWarApplication.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootWarApplication.class).run(args);
    }

}
