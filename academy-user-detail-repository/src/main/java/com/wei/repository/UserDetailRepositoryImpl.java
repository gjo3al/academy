package com.wei.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.UserDetail;

@Repository
public class UserDetailRepositoryImpl implements UserDetailRepository {

	@Autowired
	private SessionFactory factory;
	
	@Override
	public UserDetail read(int userId) {
		
		Session session = factory.getCurrentSession();

		UserDetail result;
		
		Query<UserDetail> theQuery = session.createQuery(
				"from UserDetail d join fetch d.user u where u.id=:userId", 
				UserDetail.class);
		theQuery.setParameter("userId", userId);
		
		try {
			result = theQuery.getSingleResult();
		} catch (Exception e) {
			result = null;
		}
		
		return result;
	}
	
	
}
