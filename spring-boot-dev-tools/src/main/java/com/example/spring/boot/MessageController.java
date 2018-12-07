package com.example.spring.boot;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MessageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageController.class);

    @PostConstruct
    public void slowRestart() throws InterruptedException {
        LOGGER.info("slow restart ============================================================");
        Thread.sleep(5000);
    }

    @GetMapping("/")
    public ModelAndView get(HttpSession session, HttpServletRequest request) {
        Object sessionVar = session.getAttribute("var");
        if (sessionVar == null) {
            sessionVar = new Date();
            session.setAttribute("var", sessionVar);
        }

        LOGGER.info("request URI : {}", request.getRequestURI());
        ModelMap model = new ModelMap("message", Message.MESSAGE).addAttribute("sessionVar", sessionVar);
        return new ModelAndView("hello", model);
    }

}