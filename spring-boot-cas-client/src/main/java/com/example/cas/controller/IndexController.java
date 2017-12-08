package com.example.cas.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuweijun 2017-05-17.
 */
@RestController
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping("/")
    public Map<String, String> index() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); // get logged in username
        LOGGER.info("name is : {}", name);
        
        Map<String, String> map = new HashMap<>();
        map.put("test", "login");
        map.put("date", "2017-05-17");
        return map;
    }

    @RequestMapping("/test")
    public Map<String, String> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); // get logged in username
        LOGGER.info("name is : {}", name);

        Map<String, String> map = new HashMap<>();
        map.put("test", "test");
        map.put("date", "2017-05-17");
        return map;
    }
}
