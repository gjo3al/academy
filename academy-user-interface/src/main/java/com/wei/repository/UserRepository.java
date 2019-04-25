package com.wei.repository;

import com.wei.entity.Users;

public interface UserRepository extends RepositoryInterface<Users> {
	
	Users findByUserName(String username);
	
	Users findByEmail(String email);
}
