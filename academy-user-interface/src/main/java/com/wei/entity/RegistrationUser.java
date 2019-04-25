package com.wei.entity;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.wei.validation.FieldMatch;

@FieldMatch.List({
	 @FieldMatch(first = "userInfo.users.password", second = "matchingPassword", 
			 message = "The password fields must match")
	})
public class RegistrationUser {
	
	@Valid
	private UserInfo userInfo;
	
	@NotNull(message="is required")
	private String matchingPassword;

	private List<String> authorities;
	
	public RegistrationUser() {
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public List<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}

	public String getUsername() {
		return userInfo.getUsername();
	}
	
	public String getEmail() {
		return userInfo.getEmail();
	}

}
