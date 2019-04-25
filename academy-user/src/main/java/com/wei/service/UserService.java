package com.wei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.email.EmailService;
import com.wei.entity.Users;
import com.wei.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public Users create(Users domainObject) {
		
		return userRepository.create(domainObject);
	}

	public Users update(Users domainObject) {
		return userRepository.update(domainObject);
	}

	public Users delete(String username) {
		return userRepository.delete(username);
	}

	public Users findByUserName(String username) {		
		return userRepository.findByUserName(username);
	}
	
	public Users findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public void passwordResetLink(String username) {
		
		Users theUser =  findByUserName(username);
		
		String email = theUser.getEmail();
		
		String newPassword = generatePassword(theUser);
		
		theUser.setPassword(passwordEncoder.encode(newPassword));
		
		String token = generateToken(theUser);
		
		userRepository.update(theUser);
		
		emailService.passwordResetLink(email, token, newPassword);
	}
	
	public Users verify(String email, String token) {
		
		Users theUser = userRepository.findByEmail(email);
		
		if(theUser == null)
			return null;
		
		String checkedToken = generateToken(theUser);
		
		return checkedToken.equals(token)? theUser:null;
	}
	
	public boolean isUsernameAndEmailMatched(String username, String email) {
		
		boolean matched = false;
		
		Users userByName = findByUserName(username);
		
		String userEmail = userByName.getEmail();
		
		if(userEmail.equals(email)) {
			matched = true;
		}
		
		return matched;
	}
	
	public void resetPassword(Users theUser, String newPassword) {
		
		theUser.setPassword(passwordEncoder.encode(newPassword));
		
		userRepository.update(theUser);
	}
	
	private String generateToken(Users theUser) {
		return theUser.getPassword();
	}
	
	private String generatePassword(Users theUser) {
		return passwordEncoder.encode(theUser.toString());
	}
	
	

}
