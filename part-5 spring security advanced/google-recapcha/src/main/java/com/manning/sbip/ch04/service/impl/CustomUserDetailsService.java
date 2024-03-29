package com.manning.sbip.ch04.service.impl;

import com.manning.sbip.ch04.model.ApplicationUser;
import com.manning.sbip.ch04.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

	@Value("${app.email.verification:Y}")
	private String emailVerification;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	ApplicationUser applicationUser = userService.findByUsername(username);
    	if(applicationUser == null) {
    		throw new UsernameNotFoundException("User with username "+username+" does not exists");
    	}

    	if("Y".equalsIgnoreCase(emailVerification)) {
			applicationUser.setVerified(true);
		}
    	else {
			// fake verification because of gmail not working
			applicationUser.setVerified(true);
		}

    	return User.withUsername(username).password(applicationUser.getPassword()).roles("USER").disabled(!applicationUser.isVerified()).build();
    	
    }
}
