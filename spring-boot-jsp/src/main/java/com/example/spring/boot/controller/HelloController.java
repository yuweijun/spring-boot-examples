package com.example.spring.boot.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.spring.boot.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * {@link org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter RequestMappingHandlerAdapter}
 *
 * @author yuweijun 2017-12-07
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    public String index(@ModelAttribute("time") String time, Model model) {
        System.out.println("index");
        System.out.println("time is " + time);
        model.addAttribute("hello", "yu");
        return "index";
    }

    @RequestMapping("/test")
    public String testNullView(Model model) {
        System.out.println("testNullView");
        model.addAttribute("hello", "test");
        return null;
    }

    @RequestMapping("/json")
    @ResponseBody
    public User user() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        return user;
    }

    @ModelAttribute
    public Map<String, Integer> method1() {
        System.out.println("method1");
        Map<String, Integer> map = new HashMap<>();
        map.put("test", 1);
        return map;
    }

}
