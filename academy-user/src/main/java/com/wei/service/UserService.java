package com.wei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.entity.Users;
import com.wei.repository.UserRepository;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
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

}
