package com.wei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.email.EmailService;
import com.wei.email.SecurityCodeGenerator;
import com.wei.entity.Authorities;
import com.wei.entity.RegistrationUser;
import com.wei.entity.UserDetail;
import com.wei.entity.Users;
import com.wei.repository.AuthoritiesRepositoryImpl;
import com.wei.repository.UserRepository;

@Service
@Transactional
public class RegisterService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthoritiesRepositoryImpl authoritiesRepository;

	@Autowired
	private SecurityCodeGenerator generator;
	
	@Autowired
	private EmailService emailService;
	
	final private String DEFAULT_AUTHORITY = "ROLE_STUDENT";

	// For OneToMany, to save children, we should save and commit parent first,
	// or set the parent manually
	public void register(RegistrationUser registerData) {

		Users theUser = registerUser(registerData); 
		
		String email = theUser.getUserDetail().getEmail();
		
		List<String> registerAuthorites = registerData.getAuthorities();
		
		registerAuthorites.add(DEFAULT_AUTHORITY);
		
		registerAuthorites.forEach(authName -> {
			Authorities auth = new Authorities();
			auth.setAuthority(authName);
			auth.setUser(theUser);
			authoritiesRepository.create(auth);
		});
		
		emailService.validationLink(email, generator.generateToken(theUser));
	}
	
	private Users registerUser(RegistrationUser registerData) {
		
		Users theUser = registerData.getUser();

		UserDetail detail = theUser.getUserDetail();
		
		detail.setUser(theUser);
		
		theUser.setPassword(generator.encode(theUser.getPassword()));
		
		theUser.setEnabled(false);
		
		return userRepository.create(theUser);
	}
}
