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
	public List<Course> coursesByStudentId(int studentId) {
		
		Session session = factory.getCurrentSession();
		
		Query<Course> theQuery = session.createQuery(
				"select c from Course c join c.students s where s.id=:studentId", 
				Course.class);
		
		theQuery.setParameter("studentId", studentId);
		
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

	@Override
	public void deleteStudyingCourse(int studentId, int courseId) {
		
		Session session = factory.getCurrentSession();
		
		// use native sql to delete relationship
		String theQuery = 
				String.format(
						"delete from course_student where student_id=%d and course_id=%d",
						studentId, courseId);
		
		session.createSQLQuery(theQuery).executeUpdate();
		
	}

	@Override
	public List<Course> coursesByKeyword(String keyword) {
		
		Session session = factory.getCurrentSession();
		
		Query<Course> theQuery;
		
		if(keyword == null) {
			theQuery = session.createQuery(
					"from Course c", Course.class);
		} else {
		
			theQuery = session.createQuery(
				"from Course c where lower(name) like lower(:keyword)", 
				Course.class);
		
			theQuery.setParameter("keyword", '%' + keyword + '%');
		}
		return theQuery.getResultList();
	}

	@Override
	public void registerCourse(int studentId, int courseId) {
		
		Session session = factory.getCurrentSession();
		
		if(!isTeacherOfCourse(studentId, courseId)) {
			// use native sql to delete relationship
			String theQuery = 
				String.format(
						"insert into course_student set student_id=%d, course_id=%d",
						studentId, courseId);
			
			session.createSQLQuery(theQuery).executeUpdate();
		}
	}
	
	private boolean isTeacherOfCourse(int instructorId, int courseId) {
		
		int instructorIdOfCourse = read(courseId).getInstructor().getId();
		
		return instructorId == instructorIdOfCourse;
		
	}
}
