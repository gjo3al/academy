package com.wei.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.wei.config.TestWebConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class})
@WebAppConfiguration
public class IndexControllerTest {
	
	private MockMvc mockMvc;
	
	@Before
	public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders
	    		.standaloneSetup(new IndexController())
	    		.build();
	}
	
	@Test
	public void URI_return_index() throws Exception {
		mockMvc.perform(get(""))
			.andExpect(view().name("index"));
	}
	
	@Test
	public void showLoginPageURI_return_login() throws Exception {
		mockMvc.perform(get("/showLoginPage"))
			.andExpect(view().name("login"));
	}
}
