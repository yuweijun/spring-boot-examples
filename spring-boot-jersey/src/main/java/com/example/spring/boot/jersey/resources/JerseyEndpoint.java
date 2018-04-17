package com.example.spring.boot.jersey.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

@Path("/hello")
public class JerseyEndpoint {

	@GET
	public String message() {
		return "Hello";
	}

}