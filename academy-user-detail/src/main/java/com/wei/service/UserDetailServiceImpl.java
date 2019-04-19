package com.wei.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.entity.UserDetail;
import com.wei.repository.UserDetailRepository;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailService {

	@Autowired
	private UserDetailRepository repository;
	
	@Override
	public UserDetail create(UserDetail domainObject) {

		return repository.create(domainObject);
	}

	@Override
	public UserDetail read(int id) {
		
		return repository.read(id);
	}

	@Override
	public UserDetail update(UserDetail domainObject) {
		
		return repository.update(domainObject);
	}

	@Override
	public UserDetail delete(int id) {
		
		return repository.delete(id);
	}

}
