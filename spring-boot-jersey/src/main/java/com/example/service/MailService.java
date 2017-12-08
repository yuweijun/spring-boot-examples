package com.example.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private MimeMessage mimeMessage;

	@Autowired
	private MimeMessageHelper messageHelper;

	public void send(String to) throws MessagingException {
		messageHelper.setTo(to);
		messageHelper.setSubject("test subject");
		messageHelper.setText("test by spring boot and java mail");
		this.javaMailSender.send(mimeMessage);
	}

}
