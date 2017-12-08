package com.example.spring.jsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yuweijun 2017-12-07
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String index(Model model) {
        model.addAttribute("hello", "yu");
        return "index";
    }

}
