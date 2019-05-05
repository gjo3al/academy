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
	public UserDetail read(int id) {

		Session session = factory.getCurrentSession();

		return session.get(UserDetail.class, id);
	}
}
