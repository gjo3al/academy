package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.hibernate.exception.ConstraintViolationException;
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
// run academy_test.sql every time before test
//use jpa when test(in order to support other Object Relational Mapping implement)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestConfig.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class UserRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private UserRepository userRepository;
	
	private final static String USERNAME_FOR_TEST_1 = "userForTest1";
	
	private final static String USER_EMAIL_FOR_TEST = "userForTest@gmail.com";
	
	private final static String USER_PASSWORD_FOR_TEST = "password";
	
	private final static String NEW_PASSWORD_FOR_TEST = "newPassword";
	
	private final static String USERNAME_FOR_TEST_2 = "userForTest2";
	
	private final static String DUMMY = "dummy";
	
	private final static int USER_ID_NOT_EXIST = 0;
	
	@Test
	public void create_user_success() {
		
		Users user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST_1);
		
		user.setPassword(USER_PASSWORD_FOR_TEST);
		
		userRepository.create(user);
		
		user = readByUsername(USERNAME_FOR_TEST_1);
		
		assertThat(user, notNullValue());
		assertThat(user.getUsername(), is(USERNAME_FOR_TEST_1));
		assertThat(user.getPassword(), is(USER_PASSWORD_FOR_TEST));
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void create_user_fail_duplicate_username() {
		
		createInitalUser();
		
		Users otherUser = new Users(USERNAME_FOR_TEST_1); 
		
		userRepository.create(otherUser);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void create_user_fail_duplicate_email() {
		
		createInitalUser();
		
		Users otherUser = new Users(USERNAME_FOR_TEST_2);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USER_EMAIL_FOR_TEST);
		
		detail.setUser(otherUser);
		
		userRepository.create(otherUser);
	}
	
	@Test
	public void read_user_exist() {
		
		Users user = createInitalUser();
		
		user = userRepository.read(user.getId());
		
		assertThat(user, notNullValue());
	}
	
	@Test
	public void read_user_not_exist() {
		
		Users user = userRepository.read(USER_ID_NOT_EXIST);
		
		assertThat(user, nullValue());
	}

	@Test
	public void update_user_success() {
		
		Users user = createInitalUser();
		
		user.setPassword(NEW_PASSWORD_FOR_TEST);
		
		userRepository.update(user);
		
		Users userAfterUpdate = entityManager.find(Users.class, user.getId());
		
		assertThat(userAfterUpdate.getPassword(), is(NEW_PASSWORD_FOR_TEST));
	}
	
	@Test
	public void findByUserName_exist() {
		
		createInitalUser();
		
		Users user = userRepository.findByUserName(USERNAME_FOR_TEST_1);
		
		assertThat(user, notNullValue());
	}
	
	@Test
	public void findByUserName_notExist() {
		
		Users user = userRepository.findByUserName(DUMMY);
		
		assertThat(user, nullValue());
	}
	
	@Test
	public void findByEmail_exist() {

		createInitalUser();
		
		Users user = userRepository.findByEmail(USER_EMAIL_FOR_TEST);
		
		assertThat(user, notNullValue());
	}
	
	@Test
	public void findByEmail_not_exist() {
		
		Users user = userRepository.findByEmail(DUMMY);
		
		assertThat(user, nullValue());
	}
	
	
	private Users readByUsername(String username) {
		
		Users user = null;
		
		TypedQuery<Users> theQuery = entityManager.createQuery(
				"from Users where username=:username", Users.class);
		theQuery.setParameter("username", username);
		
		try {
			user = theQuery.getSingleResult();
		} catch (Exception e) { }
		
		return user;
	}
	
	private Users createInitalUser() {
		
		Users user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST_1);
		
		user.setPassword(USER_PASSWORD_FOR_TEST);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USER_EMAIL_FOR_TEST);
		
		detail.setNickname(USERNAME_FOR_TEST_1);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
		
		entityManager.persist(user);
		
		return user;
	}
}

