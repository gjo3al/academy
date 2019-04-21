package com.wei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wei.service.AuditService;

@Controller
@RequestMapping("/audits")
public class AuditController {
	
	@Autowired
	private AuditService auditService;
	
	@GetMapping("/{username}")
	public String showAudits(@PathVariable String username) {
		
		return "audits";
	}
	
	@PostMapping("/delete/{username}")
	public String deleteAudits(@PathVariable String username) {
		
		auditService.deleteAll(username);
		
		return "redirect:/";
	}
	
}
