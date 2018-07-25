package com.example.spring.jdbc.controller;

import com.example.spring.jdbc.model.People;
import com.example.spring.jdbc.service.PeopleJdbcService;
import com.example.spring.jdbc.service.PeopleJpaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("people")
public class PeopleController {

	@Autowired
	public PeopleJpaService peopleJpaService;

	@Autowired
	private PeopleJdbcService peopleJdbcService;

	@RequestMapping("/index")
	public Collection<People> index() {
		Collection<People> found = peopleJpaService.findByFullName("test.yu");
		return found;
	}

	@RequestMapping("/all")
	public List<People> all() throws InterruptedException {
		List<People> all = peopleJpaService.findAll();
		return all;
	}

	@GetMapping("/use/jpa/transactions")
	public List<People> jpaTransactions() throws InterruptedException {
		List<People> all = peopleJpaService.useTransactions();
		return all;
	}

	@GetMapping("/without/jpa/transactions")
	public List<People> jpa() throws InterruptedException {
		List<People> all = peopleJpaService.execute();
		return all;
	}

	@GetMapping("/use/jdbc/transactions")
	public List<People> jdbcTransactions() throws InterruptedException {
		List<People> all = peopleJdbcService.useTransactions();
		return all;
	}

	@GetMapping("/without/jdbc/transactions")
	public List<People> jdbc() throws InterruptedException {
		List<People> all = peopleJdbcService.execute();
		return all;
	}
}
