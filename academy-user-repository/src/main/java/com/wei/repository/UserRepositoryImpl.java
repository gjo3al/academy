package com.wei.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.Users;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	private SessionFactory factory;

	@Override
	public Users read(int id) {

		Session session = factory.getCurrentSession();

		return session.get(Users.class, id);
	}
	
	@Override
	public Users create(Users domainObject) {

		Session session = factory.getCurrentSession();

		session.save(domainObject);

		return domainObject;
	}

	@Override
	public Users update(Users domainObject) {
		
		Session session = factory.getCurrentSession();

		session.saveOrUpdate(domainObject);

		return domainObject;
	}

	@Override
	public Users delete(String username) {
		
		Session session = factory.getCurrentSession();

		Users deleted = session.get(Users.class, username);

		session.delete(deleted);

		return deleted;
	}

	@Override
	public Users findByUserName(String username) {
		Session session = factory.getCurrentSession();
		
		Users result = null;
		
		Query<Users> theQuery = session.createQuery(
				"from Users where username=:username", Users.class);
		theQuery.setParameter("username", username);
		
		try {
			result = theQuery.getSingleResult();
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}

	@Override
	public Users findByEmail(String email) {
		Session session = factory.getCurrentSession();
		
		Users result = null;
		
		Query<Users> theQuery = session.createQuery(
				"from Users user inner join fetch user.userDetail detail "
				+ "where detail.email=:email", Users.class);
		theQuery.setParameter("email", email);
		
		try {
			result = theQuery.getSingleResult();
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}
	
}
