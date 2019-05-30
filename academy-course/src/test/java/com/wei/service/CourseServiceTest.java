package com.wei.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.wei.entity.Course;
import com.wei.repository.AuthoritiesRepository;
import com.wei.repository.CourseRepository;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceTest {
	
	@Mock
	private CourseRepository courseRepository;
	
	@Mock
	private AuthoritiesRepository authoritiesRepository;
	
	@InjectMocks
	private CourseService courseService;
	
	private final static int ID_OF_TEACHER = 1;
	
	private final static int ID_OF_STUDENT = 2;
	
	private final static String ROLE_TEACHER = "ROLE_TEACHER";
	
	private final static String COURSE_NAME = "courseForTest";
	
	private final static int ID_OF_COURSE = 1;
	
	private final static String KEYWORD = "keyword";
	
	@Test
	public void coursesByInstructorId_success() {
		
		List<Course> courses = new ArrayList<>();
		
		courses.add(new Course());
		
		given(courseRepository.coursesByInstructorId(ID_OF_TEACHER)).willReturn(courses);
		
		assertThat(courseService.coursesByInstructorId(ID_OF_TEACHER), 
				is(courses));
	}
	
	@Test
	public void coursesByStudentId_normal() {
		
		List<Course> courses = new ArrayList<>();
		
		courses.add(new Course());
		
		given(courseRepository.coursesByStudentId(ID_OF_STUDENT)).willReturn(courses);
		
		assertThat(courseService.coursesByStudentId(ID_OF_STUDENT), 
				is(courses));
	}
	
	@Test
	public void coursesByKeyword_success() {
		
		List<Course> courses = new ArrayList<>();
		
		courses.add(new Course());
		
		given(courseRepository.coursesByKeyword(KEYWORD)).willReturn(courses);
		
		assertThat(courseService.coursesByKeyword(KEYWORD), 
				is(courses));
	}
	
	@Test
	public void saveOrUpdate_success() {
		
		Course course = new Course();
		
		courseService.saveOrUpdate(course);
		
		then(courseRepository).should().update(course);
	}
	
	@Test
	public void read_success() {
		
		Course course = new Course();
		
		given(courseRepository.read(ID_OF_COURSE)).willReturn(course);
		
		assertThat(courseService.read(ID_OF_COURSE), is(course));
	}
	
	@Test
	public void delete_success() {
		
		Course course = new Course();
		
		given(courseRepository.delete(ID_OF_COURSE)).willReturn(course);
		
		assertThat(courseService.delete(ID_OF_COURSE), is(course));
		
	}
	
	@Test
	public void isTeacher_success() {
		
		given(authoritiesRepository.hasAuthority(ID_OF_TEACHER, ROLE_TEACHER))
		.willReturn(true);
		
		assertTrue(courseService.isTeacher(ID_OF_TEACHER));
	}
	
	@Test
	public void hasDuplicateCourse_duplicate() {
		
		Course course = new Course();

		course.setName(COURSE_NAME);
		
		given(courseRepository.courseByInstructorIdAndName(ID_OF_TEACHER, COURSE_NAME)).
		willReturn(course);
		
		assertTrue(courseService.hasDuplicateCourse(ID_OF_TEACHER, course));	
	}
	
	@Test
	public void hasDuplicateCourse_notDuplicate() {
		
		Course course = new Course();

		course.setName(COURSE_NAME);
		
		given(courseRepository.courseByInstructorIdAndName(ID_OF_TEACHER, COURSE_NAME)).
		willReturn(null);
		
		assertFalse(courseService.hasDuplicateCourse(ID_OF_TEACHER, course));	
	}
	
	@Test
	public void registerCourse_success() {
		courseService.registerCourse(ID_OF_STUDENT, ID_OF_COURSE);
		
		then(courseRepository).should().registerCourse(ID_OF_STUDENT, ID_OF_COURSE);
	}
	
	@Test
	public void deleteStudyingCourse_success() {
		courseService.deleteStudyingCourse(ID_OF_STUDENT, ID_OF_COURSE);
		
		then(courseRepository).should().deleteStudyingCourse(ID_OF_STUDENT, ID_OF_COURSE);
	}
}
