package com.wei.email;

public interface EmailService {
	
	public void validationLink(String email, String token);

	void passwordResetLink(String email, String token, String password);
}
