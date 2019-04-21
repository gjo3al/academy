package com.wei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/showLoginPage")
	public String showLoginPage() {
		return "login";
	}
}
