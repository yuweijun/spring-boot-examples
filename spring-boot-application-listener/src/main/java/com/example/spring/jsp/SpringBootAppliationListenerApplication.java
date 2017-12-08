package com.example.spring.jsp;

import com.example.spring.jsp.listener.ExampleListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @author yuweijun 2017-12-07
 */
@SpringBootApplication
@ServletComponentScan
public class SpringBootAppliationListenerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringBootAppliationListenerApplication.class)
                .listeners(new ExampleListener())
                .run(args);
    }

}
