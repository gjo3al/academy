package com.wei.service;

import com.wei.entity.Users;

public interface UserService extends ServiceInterface<Users> {
	Users findByUserName(String username);
}
