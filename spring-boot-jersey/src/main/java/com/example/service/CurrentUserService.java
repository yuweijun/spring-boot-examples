package com.example.service;

import com.example.model.CurrentUserDetails;

public interface CurrentUserService {
	boolean canAccessUser(CurrentUserDetails currentUserDetails, Long userId);
}