package com.example.spring.boot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * create table bookmark (id int(4) auto_increment primary key, account_id int(4) not null, uri varchar(50) default '', description varchar(255) default '', index index_of_account (account_id)) engine=InnoDB default charset utf8
 *
 */
@Entity
public class Bookmark {

	@JsonIgnore
	@ManyToOne
	private Account account;

	@Id
	@GeneratedValue
	private Long id;

	Bookmark() { // jpa only
	}

	public Bookmark(Account account, String uri, String description) {
		this.uri = uri;
		this.description = description;
		this.account = account;
	}

	public String uri;
	public String description;

	public Account getAccount() {
		return account;
	}

	public Long getId() {
		return id;
	}

	public String getUri() {
		return uri;
	}

	public String getDescription() {
		return description;
	}
}