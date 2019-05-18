package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryConfigTest;
import com.wei.entity.Audit;
import com.wei.entity.Users;

//default Transaction attribute is PROPAGATION_REQUIRED
//(support current transaction, if no transaction, create one)
//run academy_test.sql every time before test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfigTest.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class AuditRepositoryTest {

	@Autowired
	private SessionFactory factory;
	
	@Autowired
	private AuditRepository auditRepository;
	
	private final String USERNAME_FOR_TEST = "userForTest";
	
	private Users user;
	
	@Before
	public void setUpInitData() {
		createInitalUserWithAudit();
	}
	
	@Test
	public void create_new_audit() {
		
		Session session = factory.getCurrentSession();
		
		Audit audit = new Audit(user, new Timestamp(System.currentTimeMillis()+2000), "123");
		
		session.save(audit);
		
		int id = user.getId();
		
		// the return type of count() is long
		Query<Long> theQuery = session.createQuery(
				"select count(*) from Audit where user.id=:id group by user.id", Long.class);
		
		theQuery.setParameter("id", id);
		
		assertThat(theQuery.getSingleResult().intValue(), is(3));
	}
	
	@Test
	public void readAll() {
		assertThat(auditRepository.readAll(user.getId()).size(), is(2));
	}

	@Test
	public void deleteAll() {
		
		auditRepository.deleteAll(user.getId());
		
		Session session = factory.getCurrentSession();
		
		Query<Long> theQuery = session.createQuery(
				"select count(*) from Audit where user.id=:id group by user.id", Long.class);
		
		theQuery.setParameter("id", user.getId());
		
		assertThat(theQuery.getResultList().size(), is(0));
		
	}
	
	private void createInitalUserWithAudit() {
		
		Session session = factory.getCurrentSession();
		
		user = new Users();
		
		user.setUsername(USERNAME_FOR_TEST);
		
		user.setPassword(USERNAME_FOR_TEST);
		
		session.save(user);
		
		List<Audit> audits = new ArrayList<>();
		
		audits.add(new Audit(user, new Timestamp(System.currentTimeMillis()), "123"));
		audits.add(new Audit(user, new Timestamp(System.currentTimeMillis()+1000), "123"));
		
		audits.forEach(audit -> {
			session.save(audit);
		});
	}
	
}
