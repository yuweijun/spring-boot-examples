package com.example.service;

import org.springframework.stereotype.Service;

import com.example.model.CurrentUserDetails;
import com.example.model.Role;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

	@Override
	public boolean canAccessUser(CurrentUserDetails currentUserDetails, Long userId) {
		return currentUserDetails != null && (currentUserDetails.getRole() == Role.ADMIN || currentUserDetails.getId().equals(userId));
	}

}