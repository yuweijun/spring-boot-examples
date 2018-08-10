package com.example.spring.boot.controller;

import javax.servlet.http.HttpSession;

import com.example.spring.boot.service.HelloService;
import com.example.spring.boot.service.RequestScopeExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Date;

/**
 * @author yuweijun 2017-12-07
 */
@Controller
public class HelloController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private RequestScopeExample requestScopeExample;

    @Autowired
    private HelloService helloService;

    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/login")
    @ResponseBody
    public String login(Model model) {
        httpSession.setAttribute("user", "test");
        return "user login successfully.";
    }

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("hello", "yu");
        requestScopeExample.setDate(new Date());

        helloService.print();
        return "index";
    }

}
