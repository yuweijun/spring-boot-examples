package com.example.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
	Collection<Bookmark> findByAccountUsername(String username);
}