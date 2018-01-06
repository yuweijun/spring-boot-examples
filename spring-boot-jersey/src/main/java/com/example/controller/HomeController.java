package com.example.controller;

import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * http://www.tianmaying.com/tutorial/spring-boot-overview
 * 
 * You should only ever add one @EnableAutoConfiguration annotation. We generally recommend that you add it to your
 * primary @Configuration class.
 *
 */
@Controller
public class HomeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World! " + getClass().getName();
	}

	@RequestMapping("/test")
	String test(Model model) {
		model.addAttribute("message", message);
		LOGGER.info("user: {}", userService.getUserById(1));
		return "mustache/index";
	}

}