package com.example.spring.narayana;

import com.example.spring.narayana.model.Share;
import com.example.spring.narayana.model.User;
import com.example.spring.narayana.repository.ShareRepository;
import com.example.spring.narayana.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockMarketApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockMarketApplication.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShareRepository shareRepository;

    public static void main(String[] args) {
        SpringApplication.run(StockMarketApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        initUser();
        initShare();
    }

    private void initUser() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("username");
        user.setBudget(12);
        userRepository.save(user);
        LOGGER.info("user : {}", user);
    }

    private void initShare() {
        shareRepository.deleteAll();
        Share share = new Share();
        share.setSymbol("symbol");
        share.setPrice(3);
        share.setAmount(33);
        shareRepository.save(share);
        LOGGER.info("share : {}", share);
    }
}
