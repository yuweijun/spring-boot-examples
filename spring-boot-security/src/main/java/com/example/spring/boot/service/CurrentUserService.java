package com.example.spring.boot.service;

import com.example.spring.boot.model.CurrentUserDetails;

public interface CurrentUserService {
	boolean canAccessUser(CurrentUserDetails currentUserDetails, Long userId);
}