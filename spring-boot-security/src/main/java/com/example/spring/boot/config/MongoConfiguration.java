package com.example.spring.boot.config;

import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.mongodb.Mongo;

@Configuration
@ConditionalOnClass(Mongo.class)
@EnableConfigurationProperties(MongoProperties.class)
public class MongoConfiguration {

	private Mongo mongo;

	@PreDestroy
	public void close() throws UnknownHostException {
		if (this.mongo != null) {
			this.mongo.close();
		}
	}

	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.mongodb")
	public MongoProperties mongoProperties() {
		return new MongoProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public Mongo mongo() throws UnknownHostException {
		this.mongo = mongoProperties().createMongoClient(null, null);
		return this.mongo;
	}

}