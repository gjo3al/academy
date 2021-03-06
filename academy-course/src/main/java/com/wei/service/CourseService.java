package com.wei.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wei.entity.Course;
import com.wei.repository.AuthoritiesRepository;
import com.wei.repository.CourseRepository;

@Service
@Transactional
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private AuthoritiesRepository authoritiesRepository;
	
	public List<Course> coursesByInstructorId(int instructorId) {
		
		return courseRepository.coursesByInstructorId(instructorId);
		
	}
	
	public List<Course> coursesByStudentId(int studentId) {
		return courseRepository.coursesByStudentId(studentId);
	}
	
	public List<Course> coursesByKeyword(String keyword) {
		return courseRepository.coursesByKeyword(keyword);
	}
	
	public Course saveOrUpdate(Course domainObject) {
		return courseRepository.update(domainObject);
	}
	
	public Course read(int id) {
		return courseRepository.read(id);
	}
	
	public Course delete(int id) {
		return courseRepository.delete(id);
	}
	
	public boolean isTeacher(int instructorId) {
		return authoritiesRepository.hasAuthority(instructorId, "ROLE_TEACHER");
	}
	
	public boolean hasDuplicateCourse(int instructorId, Course course) {
		
		Course persistedCourse = 
				courseRepository.courseByInstructorIdAndName(instructorId, course.getName());
		
		return persistedCourse != null;
	}

	public void registerCourse(int studentId, int courseId) {
		courseRepository.registerCourse(studentId, courseId);
	}
	
	public void deleteStudyingCourse(int studentId, int courseId) {
		courseRepository.deleteStudyingCourse(studentId, courseId);
	}
	
}
