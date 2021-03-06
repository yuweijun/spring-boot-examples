package com.example.spring.boot.controller;

import java.util.Optional;

import com.example.spring.boot.model.User;
import com.example.spring.boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 * Any Spring @RestController in a Spring Boot application should render JSON response by default as long as Jackson2 is
 * on the classpath. For example:
 * 
 */
@RestController
@RequestMapping("/json")
public class JsonController {

	@Autowired
	private UserService userService;

	@RequestMapping("/")
	@ResponseBody
    User home() {
		Optional<User> user = userService.getByUsername("test");
		if (!user.isPresent()) {
			// for test.
			User newuser = new User();
			newuser.setId(1l);
			newuser.setUsername("test");
			userService.save(newuser);
			return newuser;
		}
		return user.get();
	}

}