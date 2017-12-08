package com.example.config;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
public class MailConfiguration {

	@Value("${spring.mail.user.from}")
	private String name;

	@Bean
	@ConfigurationProperties(prefix = "spring.mail")
	public JavaMailSender mailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		Properties mailProperties = sender.getJavaMailProperties();
		mailProperties.setProperty("mail.debug", "true");
		return sender;
	}

	@Bean
	public MimeMessage mimeMessage() {
		JavaMailSenderImpl sender = (JavaMailSenderImpl) mailSender();
		MimeMessage mimeMessage = sender.createMimeMessage();
		return mimeMessage;
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.mail")
	public MimeMessageHelper messageHelper() throws MessagingException {
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage(), "utf-8");
		helper.setFrom(name);
		return helper;
	}

}
