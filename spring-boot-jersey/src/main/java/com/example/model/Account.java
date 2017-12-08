package com.example.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * create table account (id int(4) auto_increment primary key, password varchar(50) not null, username varchar(50) not null) engine=InnoDB default charset utf8
 *
 */
@Entity
public class Account {

	@OneToMany(mappedBy = "account")
	private Set<Bookmark> bookmarks = new HashSet<>();

	@Id
	@GeneratedValue
	private Long id;

	@JsonIgnore
	public String password;

	public String username;

	public Set<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public Long getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public Account(String name, String password) {
		this.username = name;
		this.password = password;
	}

	Account() { // jpa only
	}
}