package com.example.spring.boot;

import com.example.spring.boot.model.Account;
import com.example.spring.boot.model.Bookmark;
import com.example.spring.boot.repository.AccountRepository;
import com.example.spring.boot.repository.BookmarkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;

@EnableAutoConfiguration
@ComponentScan
public class SpringBootSecurityApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootSecurityApplication.class);

    @Autowired
    private SecurityService service;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", "N/A",
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER")));
        try {
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println(this.service.secure());
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
            LOGGER.info(getClass().getName() + " : run");
            // jdbcTemplate.execute("truncate table user");
            jdbcTemplate.execute("truncate table account");
            jdbcTemplate.execute("truncate table bookmark");
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootSecurityApplication.class, "--debug");
    }


    /**
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

}