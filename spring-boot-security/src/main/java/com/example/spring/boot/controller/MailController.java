package com.example.spring.boot.controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.spring.boot.service.MailService;

@Controller
@RequestMapping("/mail")
public class MailController {

	@Autowired
	private MailService mailService;

	@RequestMapping("/")
	@ResponseBody
	public String index(String to) throws MessagingException {
		mailService.send(to);
		return "ok";
	}

}
