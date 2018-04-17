package com.example.spring.boot.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.example.spring.boot.dto.UserCreateForm;
import com.example.spring.boot.service.UserService;

@Component
public class UserCreateFormValidator implements Validator {

	private final UserService userService;

	@Autowired
	public UserCreateFormValidator(UserService userService) {
		this.userService = userService;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UserCreateForm.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserCreateForm form = (UserCreateForm) target;
		validatePasswords(errors, form);
		validateUsername(errors, form);
		validateEmail(errors, form);
	}

	private void validatePasswords(Errors errors, UserCreateForm form) {
		if (!form.getPassword().equals(form.getPasswordRepeated())) {
			errors.reject("password.no_match", "Passwords do not match");
		}
	}

	private void validateUsername(Errors errors, UserCreateForm form) {
		if (userService.getByUsername(form.getUsername()).isPresent()) {
			errors.reject("username.exists", "User with this username already exists");
		}
	}

	private void validateEmail(Errors errors, UserCreateForm form) {
		if (userService.getUserByEmail(form.getEmail()).isPresent()) {
			errors.reject("email.exists", "User with this email already exists");
		}
	}

}