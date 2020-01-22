package com.example.spring.narayana.service;

/**
 * @author Weijun Yu
 * @since 2020-01-22
 */
public interface PortfolioService {

    void buy(String username, String symbol, int amount);

    void sell(String username, String symbol, int amount);

}
