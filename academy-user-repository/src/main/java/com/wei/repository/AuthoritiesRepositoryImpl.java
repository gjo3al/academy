package com.wei.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.Authorities;

@Repository
public class AuthoritiesRepositoryImpl implements AuthoritiesRepository {

	@Autowired
	private SessionFactory factory;
	
	public Authorities create(Authorities domainObject) {
		
		Session session = factory.getCurrentSession();

		session.save(domainObject);

		return domainObject;
	}

	@Override
	public boolean hasAuthority(int userId, String authorityName) {
		
		Session session = factory.getCurrentSession();
		
		Query<Authorities> theQuery = session.createQuery(
				"from Authorities where user.id=:userId and authority=:authorityName", 
				Authorities.class);
		
		theQuery.setParameter("userId", userId);
		theQuery.setParameter("authorityName", authorityName);
		
		return !theQuery.getResultList().isEmpty();
	}
}
