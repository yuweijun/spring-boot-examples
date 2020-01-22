package com.example.spring.narayana.repository;

import com.example.spring.narayana.model.PortfolioEntry;
import com.example.spring.narayana.model.Share;
import com.example.spring.narayana.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PortfolioEntryRepository extends JpaRepository<PortfolioEntry, Integer> {

    PortfolioEntry findByUserAndShare(User user, Share share);

}
