package com.wei.entity;

import javax.validation.Valid;

public class UserInfo {
	
	@Valid
	private Users users;
	
	@Valid
	private UserDetail userDetail;

	public UserInfo() {
	}

	public Users getUsers() {
		return users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}
	
	public String getUsername() {
		return users.getUsername();
	}
	
	public String getEmail() {
		return userDetail.getEmail();
	}
}
