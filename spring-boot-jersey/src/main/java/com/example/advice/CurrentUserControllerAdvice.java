package com.example.advice;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CurrentUserControllerAdvice {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * you can access currentUser property in all views.
	 * 
	 * @param authentication
	 * @return
	 */
	@ModelAttribute("currentUser")
	public UserDetails getCurrentUser(Authentication authentication) {
		if (authentication == null) {
			return null;
		} else {
			Object principal = authentication.getPrincipal();
			if (principal instanceof UserDetails) {
				UserDetails user = (UserDetails) principal;
				logger.debug("get current user through @ModelAttribute: " + user.getUsername());
				Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
				for (GrantedAuthority authority : authorities) {
					logger.debug("authority.getAuthority() is: " + authority.getAuthority());
				}
				return user;
			} else if (principal instanceof String) {
				logger.debug("principal is String: " + principal);
				return null;
			} else {
				logger.info("get current principal: " + principal.getClass().getName());
				return null;
			}
		}
	}

}
