package com.wei.repository;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wei.config.RepositoryTestConfig;
import com.wei.entity.Course;
import com.wei.entity.Users;

// default Transaction attribute is PROPAGATION_REQUIRED
// (support current transaction, if no transaction, create one)
// run academy_test.sql every time before test
// use jpa when test(in order to support other Object Relational Mapping implement)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RepositoryTestConfig.class})
@Transactional
@Sql("classpath:academy_test.sql")
public class CourseRepositoryTest {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private CourseRepository courseRepository;
	
	private final static String USERNAME_FOR_TEST_TEACHER = "userForTestTeacher";
	
	private final static String USERNAME_FOR_TEST_STUDENT = "userForTestStudent";
	
	private final static String COURSE_NAME_FOR_TEST_1 = "courseForTest1";
	
	private final static String COURSE_NAME_FOR_TEST_2 = "courseForTest2";
	
	private final static String COURSE_DESCRIPTION_FOR_TEST = "description";
	
	private final static String KEYWORD_FOR_TEST = "ForTest";
	
	private final static String KEYWORD_NOT_EXIST = "dummy";
	
	private final static int ID_NOT_EXIST = 0;
	
	private Users instructor;
	
	private Users student;
	
	private List<Course> courses;
	
	public CourseRepositoryTest() {
		courses = new ArrayList<>();
	}

	@Before
	public void before() {
		createInitalUsersAndCourses();
	}
	
	// duplicate course issue is dealt with in service layer
	@Test
	public void create_new_course_success() {
			
		Course newCourse = createNewCourseWithNameAndInstructor(COURSE_NAME_FOR_TEST_2, instructor);
		
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
	public void read_course_not_exist() {
		
		Course course = courseRepository.read(ID_NOT_EXIST);
		
		assertThat(course, nullValue());
	}
	
	@Test
	public void update_course_success() {
		
		int id = courses.get(0).getId();
		
		Course course = entityManager.find(Course.class, id);
		
		course.setName(COURSE_NAME_FOR_TEST_2);
		
		course.setDescription(COURSE_DESCRIPTION_FOR_TEST);
		
		courseRepository.update(course);
		
		Course courseAfterUpdate = entityManager.find(Course.class, id);
		
		assertThat(courseAfterUpdate.getName(), is(COURSE_NAME_FOR_TEST_2));
		assertThat(courseAfterUpdate.getDescription(), is(COURSE_DESCRIPTION_FOR_TEST));
	}
	
	@Test
	public void delete_course_exist() {
		
		int id = courses.get(0).getId();
		
		courseRepository.delete(id);
		
		Course courseAfterDelete = entityManager.find(Course.class, id);
		
		assertThat(courseAfterDelete, nullValue());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void delete_course_notExist() {
		
		courseRepository.delete(ID_NOT_EXIST);
	}
	
	@Test
	public void coursesByInstructorId_exist() {
		
		List<Course> actualCourses = 
				courseRepository.coursesByInstructorId(instructor.getId());
		
		assertThat(actualCourses, is(courses));
	}
	
	@Test
	public void coursesByInstructorId_not_exist() {
		
		List<Course> actualCourses = 
				courseRepository.coursesByInstructorId(ID_NOT_EXIST);
		
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
				courseRepository.coursesByStudentId(ID_NOT_EXIST);
		
		assertThat(actualCourses.size(), is(0));
	}
	
	@Test
	public void courseByInstructorIdAndName_exist() {
		
		Course course = 
				courseRepository.courseByInstructorIdAndName(
						instructor.getId(), COURSE_NAME_FOR_TEST_1);
		
		assertThat(course, is(courses.get(0)));
	}
	
	@Test
	public void courseByInstructorIdAndName_not_exist() {
		
		Course course = 
				courseRepository.courseByInstructorIdAndName(
						instructor.getId(), COURSE_NAME_FOR_TEST_2);
		
		assertThat(course, nullValue());
	}
	
	@Test
	public void deleteStudyingCourse_exist() {
		
		courseRepository.deleteStudyingCourse(
				student.getId(), courses.get(0).getId());
		
		assertThat(getOfferedOrStudyingCourses(false).size(), is(0));
	}
	
	@Test
	public void deleteStudyingCourse_not_exist() {
		
		courseRepository.deleteStudyingCourse(
				student.getId(), ID_NOT_EXIST);
		
		assertThat(getOfferedOrStudyingCourses(false).size(), is(1));
	}
	
	@Test
	public void coursesByKeyword_exist() {
		
		assertThat(courseRepository.coursesByKeyword(KEYWORD_FOR_TEST), 
				is(courses));
	}
	
	@Test
	public void coursesByKeyword_not_exist() {
		
		assertThat(courseRepository.coursesByKeyword(KEYWORD_NOT_EXIST).size(), 
				is(0));
	}
	
	@Test
	public void coursesByKeyword_null_keyword_return_all() {

		Course newCourse = 
				createNewCourseWithNameAndInstructor(COURSE_NAME_FOR_TEST_2, instructor);
		
		entityManager.persist(newCourse);
		
		List<Course> coursesByKeyword = 
				courseRepository.coursesByKeyword(null);
		
		assertThat(coursesByKeyword.size(), is(2));
	}
	
	@Test
	public void registerCourse_success() {
		
		Course newCourse = 
				createNewCourseWithNameAndInstructor(COURSE_NAME_FOR_TEST_2, instructor);
		
		entityManager.persist(newCourse);
		
		courseRepository.registerCourse(student.getId(), newCourse.getId());
		
		courses.add(newCourse);
		
		assertThat(getOfferedOrStudyingCourses(false), is(courses));
	}
	
	private void createInitalUsersAndCourses() {
		
		instructor = new Users();
		
		instructor.setUsername(USERNAME_FOR_TEST_TEACHER);
		
		instructor.setPassword(USERNAME_FOR_TEST_TEACHER);
		
		entityManager.persist(instructor);
		
		student = new Users();
		
		student.setUsername(USERNAME_FOR_TEST_STUDENT);
		
		student.setPassword(USERNAME_FOR_TEST_STUDENT);
		
		entityManager.persist(student);
		
		Course course = createNewCourseWithNameAndInstructor(COURSE_NAME_FOR_TEST_1, instructor);
		
		entityManager.persist(course);
		
		courses.clear();
		
		courses.add(course);
		
		String theQuery = 
				String.format(
						"insert into course_student set student_id=%d, course_id=%d",
						student.getId(), course.getId());
			
		entityManager.createNativeQuery(theQuery).executeUpdate();
	}
	
	private Course createNewCourseWithNameAndInstructor(String courseName, Users instructor) {
		
		Course course = new Course();
		
		course.setName(courseName);
		
		course.setInstructor(instructor);
		
		return course;
	}
	
	private List<Course> getOfferedOrStudyingCourses(boolean forInstructor) {
		
		TypedQuery<Course> theQuery;
		
		if(forInstructor) {
			theQuery = entityManager.createQuery(
					"from Course where instructor.id=:id", 
					Course.class);
			theQuery.setParameter("id", instructor.getId());
		} else {
			theQuery = entityManager.createQuery(
					"select c from Course c join c.students s where s.id=:id", 
					Course.class);
			theQuery.setParameter("id", student.getId());
		}
		
		return theQuery.getResultList();
	}
}
