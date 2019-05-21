package com.wei.repository;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryTestConfig;
import com.wei.entity.UserDetail;
import com.wei.entity.Users;

//default Transaction attribute is PROPAGATION_REQUIRED
//(support current transaction, if no transaction, create one)
//run academy_test.sql every time before test
//use jpa when test(in order to support other Object Relational Mapping implement)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestConfig.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class UserDetailRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	private final static String USERNAME_FOR_TEST = "userForTest";
	
	private final static String USER_EMAIL_FOR_TEST = "userForTest@gmail.com";
	
	private final static int ID_NOT_EXIST = 0;
	
	private Users user;
	
	@Before
	public void before() {
		user = createInitalUser();
	}
	
	@Test
	public void read_success() {
		
		UserDetail detail = userDetailRepository.read(user.getId());
		
		assertThat(detail.getEmail(), is(USER_EMAIL_FOR_TEST));
		assertThat(detail.getNickname(), is(USERNAME_FOR_TEST));
	}
	
	@Test
	public void read_not_exist() {
		
		UserDetail detail = userDetailRepository.read(ID_NOT_EXIST);
		
		assertThat(detail, nullValue());
	}
	
	private Users createInitalUser() {
		
		Users user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST);
		
		user.setPassword(USERNAME_FOR_TEST);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USER_EMAIL_FOR_TEST);
		
		detail.setNickname(USERNAME_FOR_TEST);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
		
		entityManager.persist(user);
		
		return user;
	}
}
