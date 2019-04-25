package com.wei.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wei.entity.RegistrationUser;
import com.wei.entity.Users;
import com.wei.service.RegisterService;
import com.wei.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	private final String USER_HAS_EXISTED = "User name or email already exists."; 
	
	private final String VERIFY_SUCCESS_MESSAGE = "驗證成功，按以下連結回首頁";
	
	private final String HAS_VERIFY_MESSAGE = "此帳號已驗證，按以下連結回首頁";
	
	private final String VERIFY_FAIL_MESSAGE = "驗證失敗，按以下連結回首頁";
	
	@Autowired
	private RegisterService registerService;
	
	@Autowired
	private UserService userService;
	
	private Map<String, String> authorities; 
	
	@PostConstruct
	protected void loadAuthorities() {
		authorities = new HashMap<>();
		authorities.put("ROLE_TEACHER", "Teacher");
	}
	
	@RequestMapping("")
	public String showRegisterForm(Model theModel) {
		
		initRegistrationForm(theModel);
		
		return "register-form";
	}
	
	@PostMapping("/process")
	public String processRegistration(
			@Valid @ModelAttribute("registrationUser")RegistrationUser registrationUser, 
			BindingResult bindingResult ,Model theModel) {
		
		if(bindingResult.hasErrors()) {
			initRegistrationForm(theModel);
			return "register-form";
		}
		
		String username = registrationUser.getUsername();
		
		String email = registrationUser.getEmail();
		
		if(doesUserOrEmailExist(username, email)) {
			initRegistrationForm(theModel);
			theModel.addAttribute("registrationError", USER_HAS_EXISTED);
			return "register-form";
		}
		
		registerService.register(registrationUser);
		
		return "register-success";
	}
	
	@GetMapping("/verify")
	public String verify(
			@RequestParam String email, 
			@RequestParam String token, 
			Model theModel) {
		
		Users theUser = userService.verify(email, token);
		
		if(theUser != null) {
			if(theUser.isEnabled()) {
				theModel.addAttribute("result", HAS_VERIFY_MESSAGE);
			}else {
				theUser.setEnabled(true);
				userService.update(theUser);
				theModel.addAttribute("result", VERIFY_SUCCESS_MESSAGE);
			}
		} else {
			theModel.addAttribute("result", VERIFY_FAIL_MESSAGE);
		}
		
		return "verify-result";
	}
	
	private void initRegistrationForm(Model theModel) {
		
		theModel.addAttribute("authorities", authorities);
		
		if(!theModel.containsAttribute("registrationUser"))
			theModel.addAttribute("registrationUser", new RegistrationUser());
	}
	
	private boolean doesUserOrEmailExist(String username, String email) {
		
		return (userService.findByUserName(username) != null) || 
				(userService.findByEmail(email) != null);
	}
	
}
