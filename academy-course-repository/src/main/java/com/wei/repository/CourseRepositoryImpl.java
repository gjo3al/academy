package com.wei.repository;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wei.entity.Course;

@Repository
public class CourseRepositoryImpl implements CourseRepository {

	@Autowired
	private SessionFactory factory;
	
	@Override
	public Course create(Course domainObject) {
		
		Session session = factory.getCurrentSession();

		session.save(domainObject);

		return domainObject;
	}
	
	@Override
	public Course read(int id) {
		
		Session session = factory.getCurrentSession();

		return session.get(Course.class, id);
	}

	@Override
	public Course update(Course domainObject) {
		
		Session session = factory.getCurrentSession();

		session.saveOrUpdate(domainObject);

		return domainObject;
	}

	@Override
	public Course delete(int id) {
		
		Session session = factory.getCurrentSession();
		
		Course course = session.get(Course.class, id);
		
		session.delete(course);
		
		return course;
		
	}

	@Override
	public List<Course> coursesByInstructorId(int instructorId) {
		
		Session session = factory.getCurrentSession();
		
		Query<Course> theQuery = session.createQuery(
				"from Course where instructor.id=:id", Course.class);
		
		theQuery.setParameter("id", instructorId);
		
		return theQuery.getResultList();
	}

	@Override
	public Course courseByInstructorIdAndName(int instructorId, String courseName) {
		
		Course course = null;
		
		Session session = factory.getCurrentSession();
		
		Query<Course> theQuery = session.createQuery(
				"from Course where name=:courseName and instructor.id=:id", Course.class);
		
		theQuery.setParameter("id", instructorId);
		theQuery.setParameter("courseName", courseName);
		
		try {
			course = theQuery.getSingleResult();
		} catch (Exception e) {
			course = null;
		}
		return course;
	}

}
