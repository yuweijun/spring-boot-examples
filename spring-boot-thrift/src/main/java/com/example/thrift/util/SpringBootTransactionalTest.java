package com.example.thrift.util;

import com.example.thrift.ThriftBootApplicaton;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringApplicationConfiguration(ThriftBootApplicaton.class)
@WebIntegrationTest(randomPort = true)
public @interface SpringBootTransactionalTest {
}
