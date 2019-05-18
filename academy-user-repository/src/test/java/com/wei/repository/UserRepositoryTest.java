package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryConfigTest;
import com.wei.entity.UserDetail;
import com.wei.entity.Users;

//default Transaction attribute is PROPAGATION_REQUIRED
//(support current transaction, if no transaction, create one)
// run academy_test.sql every time before test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfigTest.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class UserRepositoryTest {

	@Autowired
	private SessionFactory factory;
	
	@Autowired
	private UserRepository userRepository;
	
	private final String USERNAME_FOR_TEST = "userForTest";
	
	private final String USERNAME_FOR_TEST_2 = "userForTest2";
	
	@Test
	public void create_user_success() {
		
		Session session = factory.getCurrentSession();
		
		Users user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST);
		
		user.setPassword(USERNAME_FOR_TEST);
		
		user.setEnabled(true);
		
		userRepository.create(user);
		
		user = readByUsername(USERNAME_FOR_TEST, session);
		
		assertThat(user, notNullValue());
		assertThat(user.getUsername(), is(USERNAME_FOR_TEST));
		assertThat(user.getPassword(), is(USERNAME_FOR_TEST));
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void create_user_fail_duplicateUsername() {
		
		createInitalUser();
		
		Users otherUser = new Users(USERNAME_FOR_TEST); 
		
		userRepository.create(otherUser);
	}
	
	@Test(expected=ConstraintViolationException.class)
	public void create_user_fail_duplicateEmail() {
		
		createInitalUser();
		
		Users otherUser = new Users(USERNAME_FOR_TEST_2);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USERNAME_FOR_TEST + "@gmail.com");
		
		detail.setUser(otherUser);
		
		userRepository.create(otherUser);
	}
	
	@Test
	public void read_user_exist() {
		
		Users user = createInitalUser();
		
		int id = user.getId();
		
		user = userRepository.read(id);
		
		assertThat(user, notNullValue());
	}
	
	@Test
	public void read_user_notExist() {
		
		Users user = userRepository.read(-1);
		
		assertThat(user, nullValue());
	}

	@Test
	public void update_user_success() {
		
		Users user = createInitalUser();
		
		user.setPassword(USERNAME_FOR_TEST_2);
		
		userRepository.update(user);
		
		int id = user.getId();
		
		Session session = factory.getCurrentSession();
		
		Users userAfterUpdate = session.get(Users.class, id);
		
		assertThat(userAfterUpdate.getPassword(), is(USERNAME_FOR_TEST_2));
	}
	
	@Test
	public void findByUserName_exist() {
		
		createInitalUser();
		
		Users user = userRepository.findByUserName(USERNAME_FOR_TEST);
		
		assertThat(user, notNullValue());
	}
	
	@Test
	public void findByUserName_notExist() {
		
		Users user = userRepository.findByUserName(USERNAME_FOR_TEST);
		
		assertThat(user, nullValue());
	}
	
	@Test
	public void findByEmail_exist() {

		createInitalUser();
		
		Users user = userRepository.findByEmail(USERNAME_FOR_TEST + "@gmail.com");
		
		assertThat(user, notNullValue());
	}
	
	@Test
	public void findByEmail_notExist() {
		
		Users user = userRepository.findByEmail(USERNAME_FOR_TEST + "@gmail.com");
		
		assertThat(user, nullValue());
	}
	
	
	private Users readByUsername(String username, Session session) {
		
		Users user = null;
		
		Query<Users> theQuery = session.createQuery(
				"from Users where username=:username", Users.class);
		theQuery.setParameter("username", username);
		
		try {
			user = theQuery.getSingleResult();
		} catch (Exception e) { }
		
		return user;
	}
	
	private Users createInitalUser() {
		
		Session session = factory.getCurrentSession();
		
		Users user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST);
		
		user.setPassword(USERNAME_FOR_TEST);
		
		UserDetail detail = new UserDetail();
		
		detail.setEmail(USERNAME_FOR_TEST + "@gmail.com");
		
		detail.setNickname(USERNAME_FOR_TEST);
		
		detail.setUser(user);
		
		user.setUserDetail(detail);
		
		session.save(user);
		
		return user;
	}
}

