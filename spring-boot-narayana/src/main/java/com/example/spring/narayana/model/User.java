package com.example.spring.narayana.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
public class User {

    /**
     * Unique username.
     */
    @Id
    private String username;

    /**
     * Current budget to buy shares.
     */
    private int budget;

    /**
     * Shares owned by this user.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<PortfolioEntry> portfolio;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Set<PortfolioEntry> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Set<PortfolioEntry> portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public String toString() {
        return String.format("User{username='%s', budget=%d, portfolio=%s}", username, budget, portfolio);
    }

}
