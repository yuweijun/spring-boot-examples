package com.example;

import com.example.model.Account;
import com.example.model.Bookmark;
import com.example.repository.AccountRepository;
import com.example.repository.BookmarkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;

/**
 * 
 * same as @Configuration @EnableAutoConfiguration @ComponentScan
 *
 */
@SpringBootApplication
public class SpringBootExampleApplication implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootExampleApplication.class);

	/**
	 * 
	 * Once started, Spring Boot will call all beans of type CommandLineRunner
	 * 
	 * @param accountRepository
	 * @param bookmarkRepository
	 * @return
	 */
	@Bean
	CommandLineRunner init(AccountRepository accountRepository, BookmarkRepository bookmarkRepository) {
		LOGGER.info(getClass().getName() + " : Bean CommandLineRunner.init");
		return (evt) -> Arrays.asList("jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(","))
				.forEach(a -> {
					Account account = accountRepository.save(new Account(a, "password"));
					bookmarkRepository
							.save(new Bookmark(account, "http://www.google.com.hk/1/" + a, "A description 1"));
					bookmarkRepository
							.save(new Bookmark(account, "http://www.google.com.hk/2/" + a, "A description 2"));
				});
	}

	public static void main(String[] args) throws Exception {
		ApplicationContext ctx = SpringApplication.run(SpringBootExampleApplication.class, args);
		String[] beanNames = ctx.getBeanDefinitionNames();
		LOGGER.info("bean sizes is: " + beanNames.length);
		// Arrays.sort(beanNames);
		// for (String beanName : beanNames) {
		// logger.info(beanName);
		// }
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	/*
	 * This Application class implements Spring Boot’s CommandLineRunner, which means it will execute the run() method
	 * after the application context is loaded up.
	 */
	@Override
	public void run(String... strings) throws Exception {
		LOGGER.info(getClass().getName() + " : run");
		// jdbcTemplate.execute("truncate table user");
		jdbcTemplate.execute("truncate table account");
		jdbcTemplate.execute("truncate table bookmark");
	}

}