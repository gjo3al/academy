package com.wei.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wei.entity.Users;
import com.wei.service.AuditService;

@Controller
@RequestMapping("/audits")
public class AuditController {
	
	@Autowired
	private AuditService auditService;
	
	@GetMapping("/{userId}")
	public String showAudits(@PathVariable int userId, HttpSession session) {
		
		Users user = (Users)session.getAttribute("user");
		
		if(user.getId() == userId) {
			return "audits";
		}
		return "redirect:/";
	}
	
	@GetMapping("/delete/{userId}")
	public String deleteAudits(@PathVariable int userId) {
		
		auditService.deleteAll(userId);
		
		return "redirect:/";
	}
	
}
