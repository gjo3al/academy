package com.wei.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.UserDetail;

@Repository
public class UserDetailRepositoryImpl implements UserDetailRepository {

	@Autowired
	private SessionFactory factory;
	
	@Override
	public UserDetail create(UserDetail domainObject) {
		
		Session session = factory.getCurrentSession();
		
		session.save(domainObject);
		
		return domainObject;
	}

	@Override
	public UserDetail read(int id) {
		
		Session session = factory.getCurrentSession();
		
		UserDetail result = session.get(UserDetail.class, id);
		
		return result;
	}

	@Override
	public UserDetail update(UserDetail domainObject) {
		
		Session session = factory.getCurrentSession();
		
		session.saveOrUpdate(domainObject);
		
		return domainObject;
	}

	@Override
	public UserDetail delete(int id) {
		
		Session session = factory.getCurrentSession();
		
		UserDetail deleted = session.get(UserDetail.class, id);
		
		session.delete(deleted);
		
		return deleted;
	}

}
