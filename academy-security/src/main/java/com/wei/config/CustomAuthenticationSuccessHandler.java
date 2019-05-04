package com.wei.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.wei.entity.Audit;
import com.wei.entity.Users;
import com.wei.service.AuditService;
import com.wei.service.UserService;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final String SHOW_AUDITS_PATH = "audits/";
	
	private final String ROOT_PATH = "http://localhost:8080/academy-web/";
	
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
		
		// redirect to previous url
		SavedRequest savedRequest = (SavedRequest)
				request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST"); 
		
		String redirectPath = 
				savedRequest == null? 
						"":savedRequest.getRedirectUrl();
		
		if(redirectPath.equals(ROOT_PATH)) redirectPath = "";
		
		String username = authentication.getName();
		
		Users user = userService.findByUserName(username);
		
		List<Audit> audits = auditService.readAll(user.getId());
		
		request.getSession().setAttribute("user", user);
		
		request.getSession().setAttribute("audits", audits);
		
		// consider if exist previous url
		if(audits.size() > 0 && redirectPath.equals("")) {
			redirectPath = SHOW_AUDITS_PATH + user.getId();
		}
		
		response.sendRedirect(redirectPath);
	}

}
