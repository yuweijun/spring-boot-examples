package com.example.spring.jdbc.test.util;

import com.example.spring.jdbc.SpringJdbcApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/integration-testing.html#integration-testing-annotations-meta
 * <p>
 * For example, if we discover that we are repeating the following configuration across our JUnit-based test suite…​
 * <code>
 *
 * @author yuweijun 2016-06-09.
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
 * @ActiveProfiles("dev")
 * @Transactional public class OrderRepositoryTests { }
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
 * @ActiveProfiles("dev")
 * @Transactional public class UserRepositoryTests { }
 * </code>
 * <p>
 * We can reduce the above duplication by introducing a custom composed annotation that centralizes the common test configuration like this:
 * <code>
 * @Target(ElementType.TYPE)
 * @Retention(RetentionPolicy.RUNTIME)
 * @ContextConfiguration({"/app-config.xml", "/test-data-access-config.xml"})
 * @ActiveProfiles("dev")
 * @Transactional public @interface TransactionalDevTest { }
 * </code>
 * Then we can use our custom @TransactionalDevTest annotation to simplify the configuration of individual test classes as follows:
 * <code>
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @TransactionalDevTest public class OrderRepositoryTests { }
 * </code>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringApplicationConfiguration(SpringJdbcApplication.class)
@WebIntegrationTest(randomPort = true)
@Transactional
//@ActiveProfiles("development")
public @interface SpringBootTransactionalTest {
}
