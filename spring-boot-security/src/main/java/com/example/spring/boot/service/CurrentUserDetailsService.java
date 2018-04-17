package com.example.spring.boot.service;

import java.util.Optional;

import com.example.spring.boot.model.CurrentUserDetails;
import com.example.spring.boot.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 
 * http://kielczewski.eu/2014/12/spring-boot-security-application/
 *
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final UserService userService;

	@Autowired
	public CurrentUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 
	 * There is often some confusion about UserDetailsService. It is purely a DAO for user data and performs no other
	 * function other than to supply that data to other components within the framework. In particular, it does not
	 * authenticate the user, which is done by the <code>AuthenticationManager</code>. In many cases it makes more sense
	 * to implement <code>AuthenticationProvider</code> directly if you require a custom authentication process.
	 * 
	 */
	@Override
	public CurrentUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info(username);
		logger.debug("username is: " + username);
		Optional<User> user = userService.getByUsername(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException(String.format("User %s does not exist!", username));
		}
		logger.debug("在下面CurrentUser构造方法中使用AuthorityUtils.createAuthorityList(user.getRole().toString())为对象设置当前用户的权限");
		return new CurrentUserDetails(user.get());
	}

}