package com.wei.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wei.entity.UserInfo;
import com.wei.entity.Users;
import com.wei.service.UserService;

@Controller
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

	@Autowired
	private UserService userService;
	
	@Override
	public Users create(Users domainObject) {
		return userService.create(domainObject);
	}

	@Override
	public Users read(String username) {
		return userService.read(username);
	}

	@Override
	public Users update(Users domainObject) {
		return userService.update(domainObject);
	}

	@Override
	public Users delete(String username) {
		return userService.delete(username);
	}
	
	@GetMapping("/register")
	private String register(Model theModel) {
		if(!theModel.containsAttribute("userInfo")) {
			theModel.addAttribute("userInfo", new UserInfo());
		}
		
		return "register-form";
	}
	
	@PostMapping("/validRegistration")
	private String validRegistration(@Valid @ModelAttribute("userInfo")UserInfo userInfo, 
			BindingResult bindingResult ,Model theModel) {
		
		if(bindingResult.hasErrors()) {
			return "register-form";
		}
		
		userInfo.getUsers().setUserDetail(userInfo.getUserDetail());
		create(userInfo.getUsers());
		
		return "register-success";
	}
}