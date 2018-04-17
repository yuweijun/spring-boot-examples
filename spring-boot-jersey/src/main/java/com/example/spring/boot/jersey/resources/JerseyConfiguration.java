package com.example.spring.boot.jersey.resources;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * 27.2 JAX-RS and Jersey
 * 
 * If you prefer the JAX-RS programming model for REST endpoints you can use one of the available implementations
 * instead of Spring MVC. Jersey 1.x and Apache CXF work quite well out of the box if you just register their Servlet or
 * Filter as a @Bean in your application context. Jersey 2.x has some native Spring support so we also provide
 * auto-configuration support for it in Spring Boot together with a starter.
 */
@Component
public class JerseyConfiguration extends ResourceConfig {

	public JerseyConfiguration() {
		register(JerseyEndpoint.class);
	}

}