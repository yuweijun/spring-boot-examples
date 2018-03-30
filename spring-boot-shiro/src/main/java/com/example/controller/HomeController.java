package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@RequestMapping({"/", "/index"})
	@ResponseBody
	String home() {
		return "Hello World! " + getClass().getName();
	}

}