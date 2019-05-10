package com.wei.repository;

import java.util.List;

import com.wei.entity.Course;

public interface CourseRepository extends RepositoryInterface<Course> {

	public List<Course> coursesByInstructorId(int instructorId);

	public List<Course> coursesByStudentId(int studentId);
	
	public Course courseByInstructorIdAndName(int instructorId, String courseName);
	
	public List<Course> coursesByKeyword(String keyword);

	public void deleteStudyingCourse(int studentId, int courseId);

	public void registerCourse(int studentId, int courseId);
}
