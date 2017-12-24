package com.example.controller;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.example.config.MvcConfiguration;

@Controller
@RequestMapping("/settings")
public class SettingsController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private MessageSource messageSource;

	@Value("${application.message:Hello World}")
	private String message = "Hello World";

	@RequestMapping("/")
	String index(ServletRequest request) {
		// 如果返回的模板名称不冲突，jspViewResolver和mustacheViewResolver可以同时存在
		logger.debug("使用jspViewResolver");
		return "boot/index";
	}

	@RequestMapping(value = "/mustache")
	String mustache(Model model) {
		logger.debug("使用mustacheViewResolver");
		model.addAttribute("message", this.message);
		return "mustache/index";
	}

	@RequestMapping(value = "/mustache/i18n")
	String i18n(HttpServletRequest request, Model model) {
		Locale locale = localeResolver.resolveLocale(request);
		logger.info(locale.getCountry());

		// 国际化
		String message = messageSource.getMessage("hello.world", null, locale);
		model.addAttribute("message", message);
		return "mustache/i18n";
	}

	/**
	 * {@link MvcConfiguration} 使用 {@link SessionLocaleResolver}
	 */
	@RequestMapping("/session")
	@ResponseBody
	public String session(HttpServletRequest request, HttpServletResponse response, String language) {
		HttpSession session = request.getSession();
		if (language.contains("en")) {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.US);
		} else {
			session.setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, Locale.CHINA);
		}

		return language;
	}

	/**
	 * {@link MvcConfiguration} 使用 {@link CookieLocaleResolver}
	 */
	@RequestMapping("/cookie")
	@ResponseBody
	public String cookie(HttpServletRequest request, HttpServletResponse response, String language) {
		if (language.contains("en")) {
			localeResolver.setLocale(request, response, Locale.US);
		} else {
			localeResolver.setLocale(request, response, Locale.CHINA);
		}

		return language;
	}

}