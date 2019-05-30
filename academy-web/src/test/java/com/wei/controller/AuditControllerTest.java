package com.wei.controller;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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

import com.wei.config.TestWebConfig;
import com.wei.entity.Audit;
import com.wei.entity.Users;
import com.wei.service.AuditService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class})
@WebAppConfiguration
public class AuditControllerTest {

	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private AuditService auditService;
	
	@InjectMocks
	private AuditController auditController;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	private MockHttpSession mockSession;
	
	private Users user;
	
	private final static int ID_OF_USER = 1;
	private final static int ID_OTHER = 0;
	
	@Before
	public void setup() {
		
		this.mockMvc = MockMvcBuilders
	    		.standaloneSetup(auditController)
	    		.build();
	    this.mockSession = new MockHttpSession(wac.getServletContext());
		
	    createInitUser();
		
		mockSession.setAttribute("user", user);
	}
	
	@Test
	public void auditsUserIdURI_success_return_audits() 
			throws Exception {
		
		mockMvc.perform(get("/audits/{userId}", ID_OF_USER)
				.session(mockSession))
			.andExpect(view().name("audits"));
	}

	@Test
	public void auditsUserIdURI_fail_redirect_index() 
			throws Exception {
		
		mockMvc.perform(get("/audits/{userId}", ID_OTHER)
				.session(mockSession))
			.andExpect(redirectedUrl("/"));
	}
	
	@Test
	public void auditsDeleteUserIdURI_success_redirect_index() 
			throws Exception {
		
		mockMvc.perform(get("/audits/delete/{userId}", ID_OF_USER))
			.andExpect(redirectedUrl("/"));
		
		then(auditService).should().deleteAll(ID_OF_USER);
	}
	
	private void createInitUser() {
		user = new Users();
		user.setId(ID_OF_USER);
	}
	
}