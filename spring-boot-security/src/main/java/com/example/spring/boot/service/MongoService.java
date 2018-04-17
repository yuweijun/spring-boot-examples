package com.example.spring.boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private MongoDbFactory mongodb;

	public String getName() {
		return mongodb.getDb().getName();
	}

	public int getSize() {
		return mongoTemplate.getCollectionNames().size();
	}

}
