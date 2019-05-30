package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryTestConfig;
import com.wei.entity.Audit;
import com.wei.entity.Users;

//default Transaction attribute is PROPAGATION_REQUIRED
//(support current transaction, if no transaction, create one)
//run academy_test.sql every time before test
//use jpa when test(in order to support other Object Relational Mapping implement)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestConfig.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class AuditRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private AuditRepository auditRepository;
	
	private final static String USERNAME = "user";
	
	private final static int USER_ID = 1;
	
	private final static int USER_ID_NOT_EXIST = 2;
	
	private Users user;
	
	@Before
	public void before() {
		createInitalUserWithAudit();
	}
	
	@Test
	public void createNewAudit_success() {
		
		Audit audit = new Audit(user, new Timestamp(System.currentTimeMillis()+2000), "123");
		
		entityManager.persist(audit);
		
		// the return type of count() is long
		TypedQuery<Long> theQuery = entityManager.createQuery(
				"select count(*) from Audit where user.id=:id group by user.id", Long.class);
		
		theQuery.setParameter("id", USER_ID);
		
		assertThat(theQuery.getSingleResult().intValue(), is(3));
	}
	
	@Test
	public void readAll_exist() {
		assertThat(auditRepository.readAll(USER_ID).size(), is(2));
	}
	
	@Test
	public void readAll_not_exist() {
		assertThat(auditRepository.readAll(USER_ID_NOT_EXIST).size(), is(0));
	}

	@Test
	public void deleteAll_success() {
		
		auditRepository.deleteAll(USER_ID);
		
		TypedQuery<Long> theQuery = entityManager.createQuery(
				"select count(*) from Audit where user.id=:id group by user.id", Long.class);
		
		theQuery.setParameter("id", USER_ID);
		
		assertThat(theQuery.getResultList().size(), is(0));
		
	}
	
	private void createInitalUserWithAudit() {
		
		user = new Users();
		
		user.setUsername(USERNAME);
		
		user.setPassword(USERNAME);
		
		entityManager.persist(user);
		
		List<Audit> audits = new ArrayList<>();
		
		audits.add(new Audit(user, new Timestamp(System.currentTimeMillis()), "123"));
		audits.add(new Audit(user, new Timestamp(System.currentTimeMillis()+1000), "123"));
		
		audits.forEach(audit -> {
			entityManager.persist(audit);
		});
	}
	
}
