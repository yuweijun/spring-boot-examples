package com.example.spring.jsp.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Created by yuweijun on 2017-12-07.
 */
@Component
public class ListenerBean {

    @EventListener
    public void handleEvent(Object event) {
        System.out.println("---------------------------------------------------");
        System.out.println(getClass().getName() + " : " + event);
        System.out.println("---------------------------------------------------");
    }

}