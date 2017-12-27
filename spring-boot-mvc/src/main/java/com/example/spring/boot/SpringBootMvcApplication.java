package com.example.spring.boot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
public class SpringBootMvcApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SpringBootMvcApplication.class);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootMvcApplication.class).run(args);
    }

}
