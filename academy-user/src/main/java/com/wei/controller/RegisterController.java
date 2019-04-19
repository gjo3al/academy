package com.wei.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wei.entity.RegistrationUser;
import com.wei.service.RegisterService;
import com.wei.service.UserService;

@Controller
@RequestMapping("/register")
public class RegisterController {

	private final String USER_HAS_EXISTED = "User name already exists."; 
	
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
		
		if(doesUserExist(username)) {
			initRegistrationForm(theModel);
			theModel.addAttribute("registrationError", USER_HAS_EXISTED);
			return "register-form";
		}
		
		registerService.register(registrationUser);
		
		return "register-success";
	}
	
	private void initRegistrationForm(Model theModel) {
		
		theModel.addAttribute("authorities", authorities);
		
		if(!theModel.containsAttribute("registrationUser"))
			theModel.addAttribute("registrationUser", new RegistrationUser());
	}
	
	private boolean doesUserExist(String username) {
		boolean exist = false;
		if(userService.findByUserName(username) != null)
			exist = true;
		return exist;
	}
}
