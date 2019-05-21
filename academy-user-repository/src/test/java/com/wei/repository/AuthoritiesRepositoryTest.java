package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryTestConfig;
import com.wei.entity.Authorities;
import com.wei.entity.Users;

//default Transaction attribute is PROPAGATION_REQUIRED
//(support current transaction, if no transaction, create one)
//run academy_test.sql every time before test
//use jpa when test(in order to support other Object Relational Mapping implement)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestConfig.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class AuthoritiesRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	private final static String AUTHORITY_NAME = "role";
	
	private final static String DUMMY = "dummy";
	
	private final static String USERNAME_FOR_TEST = "userForTest";
	
	private final static int USER_ID_FOR_TEST = 1;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	
	@Test
	public void create_success() {
		
		Authorities auth = new Authorities();
		
		auth.setAuthority(AUTHORITY_NAME);
		
		auth.setUser(createUserWith(USERNAME_FOR_TEST));
		
		authoritiesRepository.create(auth);
		
		Authorities actualAuth = entityManager.find(Authorities.class, auth);
		
		assertThat(auth, is(actualAuth));
	}
	
	@Test
	public void hasAuthority_exist() {
		
		Users user = createUserWith(USERNAME_FOR_TEST);
		
		Authorities auth = new Authorities();
		
		auth.setAuthority(AUTHORITY_NAME);
		
		auth.setUser(user);
		
		entityManager.persist(auth);
		
		assertTrue(authoritiesRepository.hasAuthority(USER_ID_FOR_TEST, AUTHORITY_NAME));
	}
	
	@Test
	public void hasAuthority_not_exist() {
		
		assertFalse(authoritiesRepository.hasAuthority(USER_ID_FOR_TEST, DUMMY));
	}

	private Users createUserWith(String username) {
		
		Users user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST);
		
		user.setPassword(USERNAME_FOR_TEST);
		
		return user;
	}
}
