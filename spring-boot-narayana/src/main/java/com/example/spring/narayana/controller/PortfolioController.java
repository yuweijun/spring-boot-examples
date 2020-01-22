package com.example.spring.narayana.controller;

import com.example.spring.narayana.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

/**
 * Portfolio controller exposing REST endpoints for buying and selling shares.
 */
@RestController
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    /**
     * <pre>
     * $> curl -X PUT "http://localhost:8080/portfolio/username/symbol?amount=2"
     * </pre>
     * <p>
     * An endpoint to buy shares.
     * <p>
     * It can be invoked with a PUT request on /portfolio/{username}/{symbol}?amount={amount} endpoint.
     * <p>
     * This method is transactional and all its work will be aborted if the following conditions are not met:
     * 1. User must exist.
     * 2. Stock must exist.
     * 3. User must have enough budget.
     * 4. There must be enough shares available to be bought.
     * <p>
     * During the execution, status messages will be sent to the "updates" queue.
     *
     * @param username A unique name of a user who's buying the shares.
     * @param symbol   A unique identifier of a share to be bought.
     * @param amount   A number of shares to be bought.
     * @throws IllegalArgumentException if a user or a share doesn't exist, or if an amount or a budget is not sufficient.
     */
    @PutMapping("/portfolio/{username}/{symbol}")
    public void buy(@PathVariable String username, @PathVariable String symbol, @RequestParam("amount") int amount) {
        portfolioService.buy(username, symbol, amount);
    }

    /**
     * <pre>
     * $> curl -X DELETE "http://localhost:8080/portfolio/username/symbol?amount=2"
     * </pre>
     * <p>
     * An endpoint to sell shares.
     * <p>
     * It can be invoked with a DELETE request on /portfolio/{username}/{symbol}?amount={amount} endpoint.
     * <p>
     * This method is transactional and all its work will be aborted if the following conditions are not met:
     * 1. User must exist.
     * 2. Stock must exist.
     * 3. User must have enough shares.
     * <p>
     * During the execution, status messages will be sent to the "updates" queue.
     *
     * @param username A unique name of a user who's selling the shares.
     * @param symbol   A unique identifier of a share to be sold.
     * @param amount   A number of shares to be sold.
     * @throws IllegalArgumentException if a user or a share doesn't exist, or if user doesn't own enough shares.
     */
    @DeleteMapping("/portfolio/{username}/{symbol}")
    @Transactional
    public void sell(@PathVariable String username, @PathVariable String symbol, @RequestParam("amount") int amount) {
        portfolioService.sell(username, symbol, amount);
    }

}
