package com.example.spring.boot.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author yuweijun 2017-12-07
 */
@Controller
public class HelloController {

    @Autowired
    private HttpSession httpSession;

    @RequestMapping("/login")
    @ResponseBody
    public String login(Model model) {
        httpSession.setAttribute("user", "test");
        return "user login successfully.";
    }

    @RequestMapping("/hello")
    public String index(Model model) {
        model.addAttribute("hello", "yu");
        return "index";
    }

}
