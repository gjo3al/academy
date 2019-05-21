package com.wei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.email.EmailService;
import com.wei.email.SecurityCodeGenerator;
import com.wei.entity.Users;
import com.wei.repository.UserDetailRepository;
import com.wei.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SecurityCodeGenerator generator;
	
	public Users read(int id) {
		
		return userRepository.read(id);
	}
	
	public Users create(Users domainObject) {
		
		return userRepository.create(domainObject);
	}

	public Users update(Users domainObject) {
		return userRepository.update(domainObject);
	}

	public Users findByUserName(String username) {
		return userRepository.findByUserName(username);
	}
	
	public Users findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public void passwordResetLink(String username) {
		
		Users theUser =  findByUserName(username);
		
		String email = userDetailRepository.read(theUser.getId()).getEmail();
		
		String newPassword = generator.generateTempPassword(theUser);
		
		theUser.setPassword(generator.encode(newPassword));
		
		String token = generator.generateToken(theUser);
		
		userRepository.update(theUser);
		
		emailService.passwordResetLink(email, token, newPassword);
	}
	
	public Users verify(String email, String token) {
		
		Users theUser = userRepository.findByEmail(email);
		
		if(theUser == null)
			return null;
		
		String checkedToken = generator.generateToken(theUser);
		
		if(checkedToken.equals(token)) {
			// fetch courses in order to offer studying courses data to JSP
			// for fear that delete studying courses after reset password
			theUser.getCourses();
			return theUser;
		} else
			return null;
	}
	
	public boolean isUsernameAndEmailMatched(String username, String email) {
		
		boolean matched = false;
		
		Users userByName = findByUserName(username);
		
		if(userByName != null) {
			String userEmail = userDetailRepository.read(userByName.getId()).getEmail();
		
			if(userEmail.equals(email)) {
				matched = true;
			}
		}
		
		return matched;
	}
	
	public void resetPassword(Users theUser, String newPassword) {
		
		theUser.setPassword(generator.encode(newPassword));
		
		userRepository.update(theUser);
	}
}
