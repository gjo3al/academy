package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryConfigTest;
import com.wei.entity.Course;
import com.wei.entity.Users;

// default Transaction attribute is PROPAGATION_REQUIRED
// (support current transaction, if no transaction, create one)
// run academy_test.sql every time before test
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryConfigTest.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class CourseRepositoryTest {

	@Autowired
	private SessionFactory factory;
	
	@Autowired
	private CourseRepository courseRepository;
	
	private final String USERNAME_FOR_TEST_TEACHER = "userForTestTeacher";
	
	private final String USERNAME_FOR_TEST_STUDENT = "userForTestStudent";
	
	private final String COURSENAME_FOR_TEST_1 = "courseForTest1";
	
	private final String COURSENAME_FOR_TEST_2 = "courseForTest2";
	
	private final String COURSEDESCRIPTION_FOR_TEST = "description";
	
	private final String EXIST_KEYWORD_FOR_TEST = "ForTest";
	
	private final String NOT_EXIST_KEYWORD_FOR_TEST = "dummy";
	
	private final int NOT_EXIST_ID = 0;
	
	private Users instructor;
	
	private Users student;
	
	private List<Course> courses;
	
	public CourseRepositoryTest() {
		courses = new ArrayList<>();
	}

	@Before
	public void setUpInitData() {
		createInitalUserWithCourse();
	}
	
	// duplicate course issue is dealt with in service layer
	@Test
	public void create_new_course_success() {
			
		Course newCourse = createNewCourseWithNameAndInstructor(COURSENAME_FOR_TEST_2, instructor);
		
		courseRepository.create(newCourse);
		
		courses.add(newCourse);
		
		assertThat(getOfferedOrStudyingCourses(true), is(courses));
	}
	
	@Test
	public void read_course_exist() {
		
		Course comparedCourse = courses.get(0);
		
		Course testCourse = courseRepository.read(comparedCourse.getId());
		
		assertThat(testCourse, is(comparedCourse));
	}
	
	@Test
	public void read_course_notExist() {
		
		Course course = courseRepository.read(NOT_EXIST_ID);
		
		assertThat(course, nullValue());
	}
	
	@Test
	public void update_course() {
		
		Session session = factory.getCurrentSession(); 
		
		int id = courses.get(0).getId();
		
		Course course = courseRepository.read(id);
		
		course.setName(COURSENAME_FOR_TEST_2);
		
		course.setDescription(COURSEDESCRIPTION_FOR_TEST);
		
		courseRepository.update(course);
		
		Course courseAfterUpdate = session.get(Course.class, id);
		
		assertThat(courseAfterUpdate.getName(), is(COURSENAME_FOR_TEST_2));
		assertThat(courseAfterUpdate.getDescription(), is(COURSEDESCRIPTION_FOR_TEST));
	}
	
	@Test
	public void delete_course_exist() {
		
		Session session = factory.getCurrentSession(); 
		
		int id = courses.get(0).getId();
		
		courseRepository.delete(id);
		
		Course courseAfterDelete = session.get(Course.class, id);
		
		assertThat(courseAfterDelete, nullValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void delete_course_notExist() {
		
		courseRepository.delete(NOT_EXIST_ID);
	}
	
	@Test
	public void coursesByInstructorId_exist() {
		
		List<Course> actualCourses = 
				courseRepository.coursesByInstructorId(instructor.getId());
		
		assertThat(actualCourses, is(courses));
	}
	
	@Test
	public void coursesByInstructorId_notExist() {
		
		List<Course> actualCourses = 
				courseRepository.coursesByInstructorId(NOT_EXIST_ID);
		
		assertThat(actualCourses.size(), is(0));
	}
	
	@Test
	public void coursesByStudentId_exist() {
		
		List<Course> actualCourses = 
				courseRepository.coursesByStudentId(student.getId());
		
		assertThat(actualCourses, is(courses));
	}
	
	@Test
	public void coursesByStudentId_notExist() {
		
		List<Course> actualCourses = 
				courseRepository.coursesByStudentId(NOT_EXIST_ID);
		
		assertThat(actualCourses.size(), is(0));
	}
	
	@Test
	public void courseByInstructorIdAndName_exist() {
		
		Course course = 
				courseRepository.courseByInstructorIdAndName(
						instructor.getId(), COURSENAME_FOR_TEST_1);
		
		assertThat(course, is(courses.get(0)));
	}
	
	@Test
	public void courseByInstructorIdAndName_notExist() {
		
		Course course = 
				courseRepository.courseByInstructorIdAndName(
						instructor.getId(), COURSENAME_FOR_TEST_2);
		
		assertThat(course, nullValue());
	}
	
	@Test
	public void deleteStudyingCourse_exist() {
		
		int studentId = student.getId();
		
		int courseId = courses.get(0).getId();
		
		courseRepository.deleteStudyingCourse(studentId, courseId);
	}
	
	@Test
	public void deleteStudyingCourse_notExist() {
		
		courseRepository.deleteStudyingCourse(NOT_EXIST_ID, NOT_EXIST_ID);
	}
	
	@Test
	public void coursesByKeyword_exist() {
		
		List<Course> coursesByKeyword = 
				courseRepository.coursesByKeyword(EXIST_KEYWORD_FOR_TEST);
		
		assertThat(coursesByKeyword, is(courses));
	}
	
	@Test
	public void coursesByKeyword_notExist() {
		
		List<Course> coursesByKeyword = 
				courseRepository.coursesByKeyword(NOT_EXIST_KEYWORD_FOR_TEST);
		
		assertThat(coursesByKeyword.size(), is(0));
	}
	
	@Test
	public void coursesByKeyword_null_keyword_return_all() {
		
		Session session = factory.getCurrentSession();
		
		Course newCourse = 
				createNewCourseWithNameAndInstructor(COURSENAME_FOR_TEST_2, instructor);
		
		session.save(newCourse);
		
		List<Course> coursesByKeyword = 
				courseRepository.coursesByKeyword(null);
		
		assertThat(coursesByKeyword.size(), is(2));
	}
	
	@Test
	public void registerCourse_success() {
		
		Session session = factory.getCurrentSession();
		
		Course newCourse = 
				createNewCourseWithNameAndInstructor(COURSENAME_FOR_TEST_2, instructor);
		
		session.save(newCourse);
		
		courseRepository.registerCourse(student.getId(), newCourse.getId());
		
		courses.add(newCourse);
		
		assertThat(getOfferedOrStudyingCourses(false), is(courses));
	}
	
	private void createInitalUserWithCourse() {
		
		Session session = factory.getCurrentSession();
		
		instructor = new Users();
		
		instructor.setUsername(USERNAME_FOR_TEST_TEACHER);
		
		instructor.setPassword(USERNAME_FOR_TEST_TEACHER);
		
		session.save(instructor);
		
		student = new Users();
		
		student.setUsername(USERNAME_FOR_TEST_STUDENT);
		
		student.setPassword(USERNAME_FOR_TEST_STUDENT);
		
		session.save(student);
		
		Course course = createNewCourseWithNameAndInstructor(COURSENAME_FOR_TEST_1, instructor);
		
		session.save(course);
		
		courses.clear();
		
		courses.add(course);
		
		String theQuery = 
				String.format(
						"insert into course_student set student_id=%d, course_id=%d",
						student.getId(), course.getId());
			
		session.createSQLQuery(theQuery).executeUpdate();
	}
	
	private Course createNewCourseWithNameAndInstructor(String courseName, Users instructor) {
		
		Course course = new Course();
		
		course.setName(courseName);
		
		course.setInstructor(instructor);
		
		return course;
	}
	
	private List<Course> getOfferedOrStudyingCourses(boolean forInstructor) {
		
		Session session = factory.getCurrentSession();
		
		Query<Course> theQuery;
		
		if(forInstructor) {
			theQuery = session.createQuery(
					"from Course where instructor.id=:id", 
					Course.class);
			theQuery.setParameter("id", instructor.getId());
		} else {
			theQuery = session.createQuery(
					"select c from Course c join c.students s where s.id=:id", 
					Course.class);
			theQuery.setParameter("id", student.getId());
		}
		
		return theQuery.getResultList();
	}
}
