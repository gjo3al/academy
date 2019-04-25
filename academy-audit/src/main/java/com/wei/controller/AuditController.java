package com.wei.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wei.entity.Users;
import com.wei.service.AuditService;

@Controller
@RequestMapping("/audits")
public class AuditController {
	
	@Autowired
	private AuditService auditService;
	
	@GetMapping("/{username}")
	public String showAudits(@PathVariable String username, HttpSession session) {
		
		Users user = (Users)session.getAttribute("user");
		
		if(user != null && user.getUsername().equals(username)) {
			return "audits";
		}
		return "redirect:/";
	}
	
	@PostMapping("/delete/{username}")
	public String deleteAudits(@PathVariable String username) {
		
		auditService.deleteAll(username);
		
		return "redirect:/";
	}
	
}
