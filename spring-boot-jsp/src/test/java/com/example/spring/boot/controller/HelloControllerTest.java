package com.example.spring.boot.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * https://spring.io/guides/gs/testing-web/
 *
 * Note the use of webEnvironment=RANDOM_PORT to start the server with a random port
 * (useful to avoid conflicts in test environments), and the injection of the port with @LocalServerPort.
 * Also note that Spring Boot has provided a TestRestTemplate for you automatically,
 * and all you have to do is @Autowired it.
 *
 * @author yuweijun
 * @date 2018-08-29.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloControllerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloControllerTest.class);

    @Autowired
    private HelloController helloController;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(helloController).isNotNull();
    }

    @Test
    public void greetingShouldReturnDefaultMessage() throws Exception {
        LOGGER.info("port is : {}", port);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/hello",
                String.class)).contains("Hello");
    }

}