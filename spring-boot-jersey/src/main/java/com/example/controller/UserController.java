package com.example.controller;

import com.example.dto.UserCreateForm;
import com.example.service.UserService;
import com.example.validator.UserCreateFormValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@Autowired
	private UserCreateFormValidator userCreateFormValidator;

	@InitBinder("form")
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userCreateFormValidator);
	}

	@PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
	@RequestMapping("/{id}")
	public ModelAndView getUserPage(@PathVariable Long id, Authentication authentication) {
		if (authentication != null) {
			UserDetails currentUser = (UserDetails) authentication.getPrincipal();
			logger.debug("currentUser username is: " + currentUser.getUsername());
		}
		return new ModelAndView("jsp/user", "user", userService.getUserById(id));
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView getUserCreatePage() {
		return new ModelAndView("jsp/user_create", "form", new UserCreateForm());
	}

	@PreAuthorize("hasAuthority('ADMIN')")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			logger.debug("errors size is: " + bindingResult.getErrorCount());
			return "jsp/user_create";
		}
		try {
			// userService.create(form);
			userService.createInNewTransaction(form);
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("error", "DataIntegrityViolationException thrown");
			return "jsp/user_create";
		}
		return "redirect:/users";
	}

}