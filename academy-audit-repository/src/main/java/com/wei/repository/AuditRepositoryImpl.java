package com.wei.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.Audit;

@Repository
public class AuditRepositoryImpl implements AuditRepository {

	@Autowired
	private SessionFactory factory;
	
	@Override
	public Audit create(Audit domainObject) {

		Session session = factory.getCurrentSession();

		session.save(domainObject);

		return domainObject;
	}
	
	@Override
	public List<Audit> readAll(int userId) {
		
		Session session = factory.getCurrentSession();
		
		Query<Audit> theQuery = session.createQuery(
				"from Audit where user.id=:userId", Audit.class);
		
		theQuery.setParameter("userId", userId);
		
		List<Audit> results = theQuery.getResultList();
		
		return results;
	}

	@Override
	public void deleteAll(int userId) {
		
		Session session = factory.getCurrentSession();
		
		@SuppressWarnings("rawtypes")
		Query theQuery = session.createQuery(
				"delete from Audit where user.id=:userId");
		
		theQuery.setParameter("userId", userId);
		
		theQuery.executeUpdate();
	}

}
