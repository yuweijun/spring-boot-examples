package com.example.spring.narayana.service;

import com.example.spring.narayana.model.PortfolioEntry;
import com.example.spring.narayana.model.Share;
import com.example.spring.narayana.model.User;
import com.example.spring.narayana.repository.PortfolioEntryRepository;
import com.example.spring.narayana.repository.ShareRepository;
import com.example.spring.narayana.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * @author Weijun Yu
 * @since 2020-01-22
 */
@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShareRepository shareRepository;

    @Autowired
    private PortfolioEntryRepository portfolioEntryRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Override
    @Transactional
    public void buy(String username, String symbol, int amount) {
        User user = getUser(username);
        Share share = getShare(symbol);
        PortfolioEntry portfolioEntry = getOrCreatePortfolioEntry(user, share); // Get user's portfolio entry or create a new one

        updateBudget(user, -1 * amount * share.getPrice()); // Decrease user's budget
        updateSharesAmount(share, -1 * amount); // Decrease shares amount available for sale
        updatePortfolioEntry(portfolioEntry, amount); // Increase shares amount owned by the user
        sendUpdate(String.format("'%s' just bought %d shares of '%s' for the amount of %d", username, amount, symbol, amount * share.getPrice()));
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
    @Override
    @Transactional
    public void sell(String username, String symbol, int amount) {
        User user = getUser(username);
        Share share = getShare(symbol);
        PortfolioEntry portfolioEntry = getPortfolioEntry(user, share);

        updateBudget(user, amount * share.getPrice()); // Increase user's budget
        updateSharesAmount(share, amount); // Increase shares amount available for sale
        updatePortfolioEntry(portfolioEntry, -1 * amount); // Decrease shares amount owned by the user
        sendUpdate(String.format("'%s' just sold %d shares of '%s' for the amount of %d", username, amount, symbol, amount * share.getPrice()));
    }


    /**
     * Update user's budget with a positive or a negative change.
     *
     * @param user   A user who's budget has to be updated.
     * @param change A positive or a negative amount to be added to the budget.
     * @throws IllegalArgumentException if a change causes user's budget to become negative.
     */
    private void updateBudget(User user, int change) {
        if (user.getBudget() + change < 0) {
            throw new IllegalArgumentException(String.format("Budget cannot be negative (%d)", user.getBudget() + change));
        }

        user.setBudget(user.getBudget() + change);
        userRepository.save(user);
        sendUpdate(String.format("Updated '%s' budget to %d", user.getUsername(), user.getBudget()));
    }

    /**
     * Update an amount of shares available in the market with a positive or a negative change.
     *
     * @param share  A share which amount has to be updated.
     * @param change A positive or a negative amount to be added to the share's amount.
     * @throws IllegalArgumentException if a change causes share's amount to become negative.
     */
    private void updateSharesAmount(Share share, int change) {
        if (share.getAmount() + change < 0) {
            throw new IllegalArgumentException(String.format("Shares amount cannot be negative (%d)", share.getAmount() + change));
        }

        share.setAmount(share.getAmount() + change);
        shareRepository.save(share);
        sendUpdate(String.format("Updated '%s' amount to %d", share.getSymbol(), share.getAmount()));
    }

    /**
     * Update user's portfolio entry with a positive or a negative change.
     *
     * @param portfolioEntry A portfolio entry to be updated.
     * @param change         A positive or negative amount to be added to the share's amount in the portfolio.
     * @throws IllegalArgumentException if a change causes share's amount to become negative.
     */
    private void updatePortfolioEntry(PortfolioEntry portfolioEntry, int change) {
        if (portfolioEntry.getAmount() + change < 0) {
            throw new IllegalArgumentException(String.format("Shares amount cannot be negative (%d)", portfolioEntry.getAmount() + change));
        }

        portfolioEntry.setAmount(portfolioEntry.getAmount() + change);
        portfolioEntryRepository.save(portfolioEntry);
    }

    /**
     * Get a user by his username.
     *
     * @param username A username of a user.
     * @return A user found by his username.
     * @throws IllegalArgumentException if a user doesn't exist.
     */
    private User getUser(String username) {
        User user = userRepository.findOne(username);
        if (user == null) {
            throw new IllegalArgumentException(String.format("User '%s' not found", username));
        }

        return user;
    }

    /**
     * Get a share by its symbol.
     *
     * @param symbol A unique symbol of a share.
     * @return A share found by its symbol.
     * @throws IllegalArgumentException if the share doesn't exist.
     */
    private Share getShare(String symbol) {
        Share share = shareRepository.findOne(symbol);
        if (share == null) {
            throw new IllegalArgumentException(String.format("Share '%s' not found", symbol));
        }

        return share;
    }

    /**
     * Get a portfolio entry by a user and a share.
     *
     * @param user  A user whose portfolio needs to be found.
     * @param share A share which portfolio needs to be found.
     * @return A portfolio entry found by its user and share.
     * @throws IllegalArgumentException if the portfolio doesn't exist.
     */
    private PortfolioEntry getPortfolioEntry(User user, Share share) {
        PortfolioEntry portfolioEntry = portfolioEntryRepository.findByUserAndShare(user, share);
        if (portfolioEntry == null) {
            throw new IllegalArgumentException("Portfolio entry not found");
        }

        return portfolioEntry;
    }

    /**
     * Get a portfolio entry by a user and a share or create one if it doesn't exist.
     *
     * @param user  A user whose portfolio needs to be found.
     * @param share A share which portfolio needs to be found.
     * @return A portfolio entry found by its user and share or a new portfolio entry.
     */
    private PortfolioEntry getOrCreatePortfolioEntry(User user, Share share) {
        PortfolioEntry portfolioEntry = portfolioEntryRepository.findByUserAndShare(user, share);

        if (portfolioEntry == null) {
            portfolioEntry = new PortfolioEntry();
            portfolioEntry.setUser(user);
            portfolioEntry.setShare(share);
        }

        return portfolioEntry;
    }

    /**
     * Send a message to the "updates" queue.
     *
     * @param message A message to be sent.
     */
    private void sendUpdate(String message) {
        jmsTemplate.convertAndSend("updates", message);
    }
}
