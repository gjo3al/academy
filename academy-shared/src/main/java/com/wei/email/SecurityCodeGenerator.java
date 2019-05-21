package com.wei.email;

public interface SecurityCodeGenerator {
	public String generateToken(Object user);
	public String generateTempPassword(Object user);
	public String encode(String rawPassword);
}
