package com.wei.entity;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.wei.validation.FieldMatch;

@FieldMatch.List({
	 @FieldMatch(first = "user.password", second = "matchingPassword", 
			 message = "The password fields must match")
	})
public class RegistrationUser {
	
	@Valid
	private Users user;
	
	@NotNull(message="is required")
	private String matchingPassword;
	
	private List<String> authorities;
	
	public RegistrationUser() {
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
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

}
