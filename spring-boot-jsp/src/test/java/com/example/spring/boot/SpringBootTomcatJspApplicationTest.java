package com.example.spring.boot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 使用 @SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) 进行
 * 测试时,你可以通过 @LocalServerPort 注解将实际端口注入到字段中
 * <p>
 * Created by yuweijun on 2017-12-08.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext
public class SpringBootTomcatJspApplicationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootTomcatJspApplicationTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testJspWithEl() {
        LOGGER.info("port is : {}", port);
        ResponseEntity<String> entity = this.restTemplate.getForEntity("/hello", String.class);
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).contains("Hello world");
    }

}