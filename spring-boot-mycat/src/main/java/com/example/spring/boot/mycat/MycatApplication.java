package com.example.spring.boot.mycat;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author yuweijun
 * @date 2018-09-06
 */
@SpringBootApplication
public class MycatApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(MycatApplication.class).web(false).run(args);
    }

}
