package com.example.spring.jdbc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author yuweijun 2017-04-23.
 */
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "redirect:people/all";
    }

}
