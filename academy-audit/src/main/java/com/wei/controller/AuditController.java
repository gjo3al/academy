package com.wei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.wei.entity.Users;
import com.wei.service.AuditService;

@Controller
@RequestMapping("/audits")
public class AuditController {

	@Autowired
	private AuditService auditService;

	@GetMapping("/{userId}")
	public String showAudits(
			@PathVariable int userId, 
			@SessionAttribute("user") Users user) {

		if (user.getId() == userId) {
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
