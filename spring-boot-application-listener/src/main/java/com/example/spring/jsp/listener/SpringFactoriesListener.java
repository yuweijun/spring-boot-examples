package com.example.spring.jsp.listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * Created by yuweijun on 2017-12-07.
 */
public class SpringFactoriesListener implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("===================================================");
        System.out.println(getClass().getName() + " : " + event);
        System.out.println("===================================================");
    }

}
