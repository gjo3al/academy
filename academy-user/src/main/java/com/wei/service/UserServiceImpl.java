package com.wei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.entity.Users;
import com.wei.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public Users create(Users domainObject) {
		
		return userRepository.create(domainObject);
	}

	@Override
	public Users read(String username) {
		return userRepository.read(username);
	}

	@Override
	public Users update(Users domainObject) {
		return userRepository.update(domainObject);
	}

	@Override
	public Users delete(String username) {
		return userRepository.delete(username);
	}

	@Override
	public Users findByUserName(String username) {		
		return userRepository.findByUserName(username);
	}

}
