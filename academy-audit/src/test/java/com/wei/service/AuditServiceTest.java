package com.wei.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.wei.entity.Audit;
import com.wei.entity.Users;
import com.wei.repository.AuditRepository;

@RunWith(MockitoJUnitRunner.class)
public class AuditServiceTest {

	@Mock
	private AuditRepository auditRepository;

	@InjectMocks
	private AuditService auditService;

	@Captor
	ArgumentCaptor<Audit> auditCaptor;

	private Users user;
	private List<Audit> audits;

	private final static String USERNAME = "user";
	private final static int USER_ID = 1;
	private final static int USER_ID_NOT_EXIST = 2;
	private final static String ADDRESS = "0.0.0.0";

	@Before
	public void before() {
		createInitUserWithAudits();
	}

	@Test
	public void create_success() {
		auditService.create(user, ADDRESS);
		then(auditRepository).should().create(auditCaptor.capture());
	}

	@Test
	public void readAll_exist() {
		given(auditRepository.readAll(USER_ID)).willReturn(audits);
		assertThat(auditService.readAll(USER_ID), is(audits));
	}

	@Test
	public void readAll_not_exist() {
		List<Audit> emptyList = new ArrayList<>();
		given(auditRepository.readAll(USER_ID_NOT_EXIST)).willReturn(emptyList);
		assertThat(auditService.readAll(USER_ID_NOT_EXIST).size(), is(0));
	}

	@Test
	public void deleteAll_success() {
		auditService.deleteAll(USER_ID);
		then(auditRepository).should().deleteAll(USER_ID);
	}

	private void createInitUserWithAudits() {
		
		user = new Users();
		user.setUsername(USERNAME);
		user.setPassword(USERNAME);

		audits = new ArrayList<>();
		audits.add(new Audit(user, new Timestamp(System.currentTimeMillis()), "123"));
		audits.add(new Audit(user, new Timestamp(System.currentTimeMillis() + 1000), "123"));
	}
}
