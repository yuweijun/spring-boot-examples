package com.example.spring.boot;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { SpringBootSecurityApplication.class, SpringBootSecurityApplicationTests.TestConfiguration.class })
public class SpringBootSecurityApplicationTests {

    @Autowired
    private SecurityService service;

    @Autowired
    private ApplicationContext context;

    private Authentication authentication;

    @Before
    public void init() {
        AuthenticationManager authenticationManager = this.context.getBean(AuthenticationManager.class);
        this.authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("user", "password"));
    }

    @After
    public void close() {
        SecurityContextHolder.clearContext();
    }

    @Test(expected = AuthenticationException.class)
    public void secure() throws Exception {
        assertThat("Hello Security").isEqualTo(this.service.secure());
    }

    @Test
    public void authenticated() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(this.authentication);
        assertThat("Hello Security").isEqualTo(this.service.secure());
    }

    @Test
    public void preauth() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(this.authentication);
        assertThat("Hello World").isEqualTo(this.service.authorized());
    }

    @Test(expected = AccessDeniedException.class)
    public void denied() throws Exception {
        SecurityContextHolder.getContext().setAuthentication(this.authentication);
        assertThat("Goodbye World").isEqualTo(this.service.denied());
    }

    @PropertySource("classpath:test.properties")
    @Configuration
    protected static class TestConfiguration {
    }

}