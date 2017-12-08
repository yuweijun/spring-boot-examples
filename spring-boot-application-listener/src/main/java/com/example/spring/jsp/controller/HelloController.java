package com.example.spring.jsp.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuweijun 2017-12-07
 */
@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String index(Model model) {
        return "hello";
    }

}
