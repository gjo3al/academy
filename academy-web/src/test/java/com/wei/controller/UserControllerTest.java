package com.wei.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.wei.config.GlobalInitializer;
import com.wei.config.TestWebConfig;
import com.wei.entity.Users;
import com.wei.service.RegisterService;
import com.wei.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class})
@WebAppConfiguration
public class UserControllerTest {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private UserService userService;
	
	@Mock
	private RegisterService registerService;
	
	@InjectMocks
	private UserController userController;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	private MockHttpSession mockSession;
	
	private final static int ID_USER = 1;
	private final static String USER_NAME = "user";
	private final static String USER_PASSWORD = "password";
	private final static String USER_EMAIL = "user@gmail.com";
	private final static String TOKEN = "token";
	private final static String ANY_STRING = "any";
	
	@Before
	public void setup() throws Exception {
		
	    this.mockMvc = MockMvcBuilders
	    		.standaloneSetup(userController)
	    		.setControllerAdvice(new GlobalInitializer())
	    		.build();
	    this.mockSession = new MockHttpSession(wac.getServletContext());
		
		Users user = new Users();
		user.setId(ID_USER);
		
		mockSession.setAttribute("user", user);		
	}
	
	@Test
	public void usersForgetGetURL_return_forgetPassword() 
			throws Exception {
		
		mockMvc.perform(get("/users/forget"))
			.andExpect(view().name("forget-password"));
	}
	
	@Test
	public void usersForgetPostURL_usernameIsNull_return_forgetPassword() 
			throws Exception {
		
		mockMvc.perform(post("/users/forget")
				.param("username", "")
				.param("email", USER_EMAIL))
			.andExpect(model().attributeExists("forgetError"))
			.andExpect(view().name("forget-password"));
	}
	
	@Test
	public void usersForgetPostURL_emailIsNull_return_forgetPassword() 
			throws Exception {
		
		mockMvc.perform(post("/users/forget")
				.param("username", USER_NAME)
				.param("email", ""))
			.andExpect(model().attributeExists("forgetError"))
			.andExpect(view().name("forget-password"));
	}

	@Test
	public void usersForgetPostURL_usernameAndEmailNotMatched_return_forgetPassword() 
			throws Exception {
		
		given(userService.isUsernameAndEmailMatched(USER_NAME, USER_EMAIL)).willReturn(false);
		
		mockMvc.perform(post("/users/forget")
				.param("username", USER_NAME)
				.param("email", USER_EMAIL))
			.andExpect(model().attributeExists("forgetError"))
			.andExpect(view().name("forget-password"));
	}
	
	@Test
	public void usersForgetPostURL_success_return_notation() 
			throws Exception {
		
		given(userService.isUsernameAndEmailMatched(USER_NAME, USER_EMAIL)).willReturn(true);
		
		mockMvc.perform(post("/users/forget")
				.param("username", USER_NAME)
				.param("email", USER_EMAIL))
			.andExpect(model().attributeExists("title", "message"))
			.andExpect(view().name("notation"));
		
		then(userService).should().passwordResetLink(USER_NAME);
	}
	
	@Test
	public void usersResetGetURL_verifyFail_redirect_index() 
			throws Exception {
		
		given(userService.verify(USER_EMAIL, TOKEN)).willReturn(null);
		
		mockMvc.perform(get("/users/reset")
				.param("email", USER_EMAIL)
				.param("token", TOKEN))
			.andExpect(redirectedUrl("/"));
	}
	
	@Test
	public void usersResetGetURL_verifySuccess_return_resetPasswordForm() 
			throws Exception {
		
		Users user = new Users();
		
		given(userService.verify(USER_EMAIL, TOKEN)).willReturn(user);
		
		mockMvc.perform(get("/users/reset")
				.param("email", USER_EMAIL)
				.param("token", TOKEN))
			.andExpect(model().attribute("user", user))
			.andExpect(view().name("reset-password-form"));
	}
	
	@Test
	public void usersResetPostURL_passwordIsNull_return_resetPasswordForm() 
			throws Exception {
		
		Users user = new Users();
		
		mockMvc.perform(post("/users/reset")
				.param("newPassword", "")
				.param("matchingPassword", USER_PASSWORD)
				.flashAttr("user", user))
			.andExpect(model().attributeExists("resetError"))
			.andExpect(model().attribute("user", user))
			.andExpect(view().name("reset-password-form"));
	}
	
	@Test
	public void usersResetPostURL_passwordNotMatched_return_resetPasswordForm() 
			throws Exception {
		
		Users user = new Users();
		
		mockMvc.perform(post("/users/reset")
				.param("newPassword", ANY_STRING)
				.param("matchingPassword", USER_PASSWORD)
				.flashAttr("user", user))
			.andExpect(model().attributeExists("resetError"))
			.andExpect(model().attribute("user", user))
			.andExpect(view().name("reset-password-form"));
	}
	
	@Test
	public void usersResetPostURL_success_return_notation() 
			throws Exception {
		
		Users user = new Users();
		
		mockMvc.perform(post("/users/reset")
				.param("newPassword", USER_PASSWORD)
				.param("matchingPassword", USER_PASSWORD)
				.flashAttr("user", user))
			.andExpect(model().attributeExists("title", "message"))
			.andExpect(view().name("notation"));
	}
}
