package com.example.spring.jdbc.controller;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.service.PeopleJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("people")
public class PeopleController {

	@Autowired
	public PeopleJpaService peopleJpaService;

	@RequestMapping("/index")
	@ResponseBody
	public Collection<People> index() {
		Collection<People> found = peopleJpaService.findByFullName("test.yu");
		return found;
	}

	@RequestMapping("/all")
	@ResponseBody
	public List<People> all() throws InterruptedException {
		List<People> all = peopleJpaService.findAll();
		return all;
	}

}
