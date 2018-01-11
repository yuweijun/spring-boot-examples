package com.example.spring.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter RequestMappingHandlerAdapter}
 *
 * @author yuweijun 2017-12-07
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String index(Model model) {
        model.addAttribute("hello", "yu");
        return "index";
    }

    @RequestMapping("/test")
    public String testNullView(Model model) {
        model.addAttribute("hello", "test");
        return null;
    }

}
