package com.wei.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.wei.entity.Audit;
import com.wei.entity.Users;
import com.wei.service.AuditService;
import com.wei.service.UserService;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final String SHOW_AUDITS_PATH = "audits/";
	
	@Autowired
	private AuditService auditService;
	
	@Autowired
	private UserService userService;
	
	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication)
			throws IOException, ServletException {

		String redirectPath = "";
		
		String username = authentication.getName();
		
		Users user = userService.findByUserName(username);
		
		List<Audit> audits = auditService.readAll(username);
		
		request.getSession().setAttribute("user", user);
		
		request.getSession().setAttribute("audits", audits);
		
		if(audits.size() > 0) {
			redirectPath = SHOW_AUDITS_PATH + username;
		}
		
		response.sendRedirect(redirectPath);
	}

}
