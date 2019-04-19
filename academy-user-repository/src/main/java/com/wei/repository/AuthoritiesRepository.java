package com.wei.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.Authorities;

@Repository
public class AuthoritiesRepository {

	@Autowired
	private SessionFactory factory;
	
	public Authorities create(Authorities domainObject) {
		
		Session session = factory.getCurrentSession();

		session.save(domainObject);

		return domainObject;
	}
}
