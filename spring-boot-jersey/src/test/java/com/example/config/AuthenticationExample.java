package com.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationExample {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationExample.class);

	private static AuthenticationManager am = new SampleAuthenticationManager();

	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			// 输入的用户名密码一样时，通过验证
			LOGGER.info("Please enter your username:");
			String name = in.readLine();
			LOGGER.info("Please enter your password:");
			String password = in.readLine();
			try {
				Authentication request = new UsernamePasswordAuthenticationToken(name, password);
				Authentication result = am.authenticate(request);
				SecurityContextHolder.getContext().setAuthentication(result);
				break;
			} catch (AuthenticationException e) {
				LOGGER.info("Authentication failed: " + e.getMessage());
			}
		}
		LOGGER.info("Successfully authenticated. Security context contains: "
				+ SecurityContextHolder.getContext().getAuthentication());
	}
}

class SampleAuthenticationManager implements AuthenticationManager {
	static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();

	static {
		AUTHORITIES.add(new SimpleGrantedAuthority("ROLE_USER"));
	}

	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		if (auth.getName().equals(auth.getCredentials())) {
			return new UsernamePasswordAuthenticationToken(auth.getName(), auth.getCredentials(), AUTHORITIES);
		}
		throw new BadCredentialsException("Bad Credentials");
	}
}