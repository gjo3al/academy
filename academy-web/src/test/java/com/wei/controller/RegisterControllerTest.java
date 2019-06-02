package com.wei.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.wei.config.GlobalInitializer;
import com.wei.config.TestWebConfig;
import com.wei.entity.RegistrationUser;
import com.wei.entity.UserDetail;
import com.wei.entity.Users;
import com.wei.service.RegisterService;
import com.wei.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class})
@WebAppConfiguration
public class RegisterControllerTest {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private UserService userService;
	
	@Mock
	private RegisterService registerService;
	
	@InjectMocks
	private RegisterController registerController;
	
	private MockMvc mockMvc;
	
	private final static String USER_NAME = "user";
	private final static String USER_EMAIL = "user@gmail.com";
	private final static String TOKEN = "token";
	
	@Before
	public void setup() throws Exception {
		
	    this.mockMvc = MockMvcBuilders
	    		.standaloneSetup(registerController)
	    		.setControllerAdvice(new GlobalInitializer())
	    		.build();
	}
	
	@Test
	public void registerGetURL_return_registerForm() 
			throws Exception {
		mockMvc.perform(get("/register"))
			.andExpect(model().attributeExists("registrationUser"))
			.andExpect(view().name("register-form"));
	}
	
	@Test
	public void registerPostURL_usernameOrEmailExist_return_registerForm() 
			throws Exception {
		
		Users user = createUserWithDetail(USER_NAME, USER_EMAIL);
		
		RegistrationUser registrationUser = new RegistrationUser();
		registrationUser.setUser(user);
		
		given(userService.findByUserName(USER_NAME)).willReturn(user);
		
		mockMvc.perform(post("/register")
				.flashAttr("registrationUser", registrationUser))
			.andExpect(model().attributeExists("registrationUser", "registrationError"))
			.andExpect(view().name("register-form"));
	}
	
	@Test
	public void registerPostURL_success_return_notation() 
			throws Exception {
		
		Users user = createUserWithDetail(USER_NAME, USER_EMAIL);
		
		RegistrationUser registrationUser = new RegistrationUser();
		registrationUser.setUser(user);
		
		given(userService.findByUserName(USER_NAME)).willReturn(null);
		given(userService.findByEmail(USER_EMAIL)).willReturn(null);
		
		mockMvc.perform(post("/register")
				.flashAttr("registrationUser", registrationUser))
			.andExpect(model().attributeExists("registrationUser", "title", "message"))
			.andExpect(view().name("notation"));
		
		then(registerService).should().register(registrationUser);
	}
	
	@Test
	public void registerVerifyURL_verifyFail_return_notation() 
			throws Exception {
		
		given(userService.verify(USER_EMAIL, TOKEN)).willReturn(null);
		
		mockMvc.perform(get("/register/verify")
				.param("email", USER_EMAIL)
				.param("token", TOKEN))
			.andExpect(view().name("notation"));
		
		then(userService).should(never()).update(any());
	}
	
	@Test
	public void registerVerifyURL_hasVerify_return_notation() 
			throws Exception {
		
		Users user = createUserWithDetail(USER_NAME, USER_EMAIL);
		user.setEnabled(true);
		
		given(userService.verify(USER_EMAIL, TOKEN)).willReturn(user);
		
		mockMvc.perform(get("/register/verify")
				.param("email", USER_EMAIL)
				.param("token", TOKEN))
			.andExpect(view().name("notation"));
		
		then(userService).should(never()).update(any());
	}
	
	@Test
	public void registerVerifyURL_success_return_notation() 
			throws Exception {
		
		Users user = createUserWithDetail(USER_NAME, USER_EMAIL);
		
		given(userService.verify(USER_EMAIL, TOKEN)).willReturn(user);
		
		mockMvc.perform(get("/register/verify")
				.param("email", USER_EMAIL)
				.param("token", TOKEN))
			.andExpect(view().name("notation"));
		
		then(userService).should().update(user);
		assertTrue(user.isEnabled());
	}

	private Users createUserWithDetail(String username, String email) {
		
		Users user = new Users(username);
		
		UserDetail detail = new UserDetail();
		detail.setEmail(email);
		user.setUserDetail(detail);
		
		return user;
	}
}
