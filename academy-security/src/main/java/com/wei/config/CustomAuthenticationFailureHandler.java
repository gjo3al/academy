package com.wei.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.wei.entity.Users;
import com.wei.service.AuditService;
import com.wei.service.UserService;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuditService auditService;
	
	@Override
	public void onAuthenticationFailure(
			HttpServletRequest request, 
			HttpServletResponse response, 
			AuthenticationException exception) 
			throws IOException, ServletException {
	
		String username = request.getParameter("username");
		
		Users user = userService.findByUserName(username);
		
		if(user != null) {
			writeAudit(user, request.getRemoteAddr());
		}
		
		request.getSession().setAttribute("error", "");
		response.sendRedirect("showLoginPage");
	}

	private void writeAudit(Users user, String address) {
		auditService.create(user, address);
	}
}
