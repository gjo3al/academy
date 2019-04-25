package com.wei.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wei.entity.Users;
import com.wei.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/forget")
	public String forgetPassword() {
		return "forget-password";
	}
	
	@PostMapping("/forget/process")
	public String forgetPasswordProcess(
			@RequestParam String username, 
			@RequestParam String email,
			Model theModel
			) {
		
		String path = "reset-notation";
		
		if(username == null || email == null
			|| !userService.isUsernameAndEmailMatched(username, email)) {
			theModel.addAttribute("error", "");
			path = "forget-password";
		} else {
			userService.passwordResetLink(username);
		}
	
		return path;
	}
	
	@GetMapping("/reset")
	public String reset(
			@RequestParam String email, 
			@RequestParam String token,
			Model theModel) {
		
		Users theUser = userService.verify(email, token);
		
		if(theUser != null) {
			theModel.addAttribute("user", theUser);
			return "reset-password-form";
		}
		
		return "redirect:/";
	}
	
	@PostMapping("/reset/process")
	public String resetPasswordProcess(
			@ModelAttribute("user") Users theUser,
			@RequestParam String newPassword, 
			@RequestParam String matchingPassword,
			HttpServletRequest request,
			Model theModel) {
		
		String path;
		
		if(newPassword == null || !newPassword.equals(matchingPassword)) {
			theModel.addAttribute("error", "");
			theModel.addAttribute("user", theUser);
			path = "reset-password-form";
		} else {
			userService.resetPassword(theUser, newPassword);
			try {
				request.logout();
			} catch (ServletException e) {
				e.printStackTrace();
			}
			path = "login";
		}
		
		return path;
	}
	
}
