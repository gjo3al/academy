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

	private final String SEND_NEW_PASSWORD_SUCCESS_TITLE = "寄送新密碼";
	
	private final String SEND_NEW_PASSWORD_SUCCESS_MESSAGE = "已寄送新密碼，請至電子信箱中確認";
	
	private final String RESET_SUCCESS_TITLE = "更改密碼";
	
	private final String RESET_SUCCESS_MESSAGE = "已更改密碼，請回到首頁重新登入";
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/forget")
	public String forgetPassword() {
		return "forget-password";
	}
	
	@PostMapping("/forget")
	public String forgetPasswordProcess(
			@RequestParam String username, 
			@RequestParam String email,
			Model theModel
			) {
		
		String path = "notation";
		
		if(username == null || email == null
			|| !userService.isUsernameAndEmailMatched(username, email)) {
			theModel.addAttribute("forgetError", "");
			path = "forget-password";
		} else {
			userService.passwordResetLink(username);
			theModel.addAttribute("title", SEND_NEW_PASSWORD_SUCCESS_TITLE);
			theModel.addAttribute("message", SEND_NEW_PASSWORD_SUCCESS_MESSAGE);
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
	
	@PostMapping("/reset")
	public String resetPasswordProcess(
			@ModelAttribute("user") Users theUser,
			@RequestParam String newPassword, 
			@RequestParam String matchingPassword,
			HttpServletRequest request,
			Model theModel) {
		
		String path;
		
		if(newPassword == null || !newPassword.equals(matchingPassword)) {
			theModel.addAttribute("resetError", "");
			theModel.addAttribute("user", theUser);
			path = "reset-password-form";
		} else {
			userService.resetPassword(theUser, newPassword);
			
			//重設密碼後必須以新密碼登入
			try {
				request.logout();
				theModel.addAttribute("title", RESET_SUCCESS_TITLE);
				theModel.addAttribute("message", RESET_SUCCESS_MESSAGE);
			} catch (ServletException e) {
				e.printStackTrace();
			}
			path = "notation";
		}
		
		return path;
	}
	
}
