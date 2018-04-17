package com.example.spring.boot.service;

import com.example.spring.boot.model.CurrentUserDetails;
import com.example.spring.boot.model.Role;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl implements CurrentUserService {

	@Override
	public boolean canAccessUser(CurrentUserDetails currentUserDetails, Long userId) {
		return currentUserDetails != null && (currentUserDetails.getRole() == Role.ADMIN || currentUserDetails.getId().equals(userId));
	}

}