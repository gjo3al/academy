package com.wei.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.wei.entity.Users;

@Component
public class SimpleSecurityCodeGenerator implements SecurityCodeGenerator {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public String generateToken(Object user) {
		
		Users theUser = (Users) user;
		
		return theUser.getPassword();
	}

	@Override
	public String generateTempPassword(Object user) {
		
		Users theUser = (Users) user;
		
		return passwordEncoder.encode(theUser.toString());
	}

	@Override
	public String encode(String rawPassword) {
		
		return passwordEncoder.encode(rawPassword);
	}

}
