package com.wei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wei.entity.UserDetail;
import com.wei.service.UserDetailService;

@Controller
@RequestMapping("/userDetail")
public class UserDetailControllerImpl implements UserDetailController {

	@Autowired
	private UserDetailService userDetailService;

	@Override
	public UserDetail create(UserDetail domainObject) {
		return userDetailService.create(domainObject);
	}

	@Override
	public UserDetail read(int id) {
		return userDetailService.read(id);
	}

	@Override
	public UserDetail update(UserDetail domainObject) {
		return userDetailService.update(domainObject);
	}

	@Override
	public UserDetail delete(int id) {
		return userDetailService.delete(id);
	}

}
