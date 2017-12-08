package com.example.model;

import org.springframework.security.core.authority.AuthorityUtils;

public class CurrentUserDetails extends org.springframework.security.core.userdetails.User {

	private static final long serialVersionUID = 8950017146947875468L;

	private User user;

	public CurrentUserDetails(User user) {
		// 这里就是把我们从数据库里面取得的用户权限和Spring Security的配置进行桥接
		super(user.getUsername(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	public Role getRole() {
		return user.getRole();
	}

}