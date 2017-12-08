package com.example.controller;

import com.example.service.MongoService;
import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/mongo")
public class MongoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MongoController.class);

	@Autowired
	private ApplicationArguments applicationArguments;

	@Autowired
	private Mongo mongo;

	@Autowired
	private MongoService mongoService;

	@RequestMapping("/host")
	@ResponseBody
	String host() {
		List<String> list = applicationArguments.getNonOptionArgs();
		LOGGER.info("{}", list.size());

		String host = mongo.getAddress().getHost();
		return "mongo host is " + host;
	}

	@RequestMapping("/")
	@ResponseBody
	String index() {
		LOGGER.info("mongo index request.");
		return "get collection size: " + mongoService.getSize();
	}

	@RequestMapping("/name")
	@ResponseBody
	String name() {
		return mongoService.getName();
	}
}
