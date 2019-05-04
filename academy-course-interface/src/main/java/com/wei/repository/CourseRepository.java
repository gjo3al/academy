package com.wei.repository;

import java.util.List;

import com.wei.entity.Course;

public interface CourseRepository extends RepositoryInterface<Course> {

	public List<Course> coursesByInstructorId(int instructorId);

	public Course courseByInstructorIdAndName(int instructorId, String courseName);
	
}
