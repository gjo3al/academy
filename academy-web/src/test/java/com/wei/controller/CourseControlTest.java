package com.wei.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.wei.config.TestWebConfig;
import com.wei.entity.Course;
import com.wei.entity.Users;
import com.wei.service.AuditService;
import com.wei.service.CourseService;
import com.wei.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestWebConfig.class})
@WebAppConfiguration
public class CourseControlTest {
	
	@Rule
	public MockitoRule rule = MockitoJUnit.rule();
	
	@Mock
	private CourseService courseService;
	
	@Mock
	private UserService userService;
	
	@Mock
	private AuditService auditService;
	
	@InjectMocks
	private CourseController courseController;
	
	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	private MockHttpSession mockSession;
	
	private final static int ID_OTHER = 0;
	private final static int ID_NEW = 0;
	private final static int ID_OF_TEACHER = 1;
	private final static int ID_OF_STUDENT = 2;
	
	private final static int ID_OF_COURSE = 1;
	private final static String ANY_NAME = "name";
	private final static String KEYWORD = "keyword";
	
	@Before
	public void setup() throws Exception {
		
	    this.mockMvc = MockMvcBuilders
	    		.standaloneSetup(courseController)
	    		.build();
	    this.mockSession = new MockHttpSession(wac.getServletContext());
		
		Users user = new Users();
		user.setId(ID_OF_STUDENT);
		
		mockSession.setAttribute("user", user);		
	}
	
	@Test
	public void coursesInstructorIdOfferURI_instructorNotExist_return_offeredCourses() 
			throws Exception {
		
		given(userService.read(ID_OTHER)).willReturn(null);
		
		mockMvc.perform(get("/courses/{instructorId}/offer", ID_OTHER).session(mockSession))
			.andExpect(view().name("offered-courses"));
		
		then(userService).should().read(ID_OTHER);
	}
	
	@Test
	public void coursesInstructorIdOfferURI_notInstructor_return_offeredCourses() 
			throws Exception {
		
		Users user = new Users();
		user.setId(ID_OF_STUDENT);
		
		given(userService.read(ID_OF_STUDENT)).willReturn(user);
		given(courseService.isTeacher(ID_OF_STUDENT)).willReturn(false);
		
		mockMvc.perform(get("/courses/{instructorId}/offer", ID_OF_STUDENT).session(mockSession))
			.andExpect(model().attributeDoesNotExist("isSelf"))
			.andExpect(view().name("offered-courses"));
		then(userService).should().read(ID_OF_STUDENT);
	}
	
	@Test
	public void coursesInstructorIdOfferURI_correctInstructor_return_offeredCourses() 
			throws Exception {
		
		Users user = new Users();
		user.setId(ID_OF_TEACHER);
		mockSession.setAttribute("user", user);
		
		List<Course> courses = Arrays.asList(new Course());
		
		given(userService.read(ID_OF_TEACHER)).willReturn(user);
		given(courseService.isTeacher(ID_OF_TEACHER)).willReturn(true);
		given(courseService.coursesByInstructorId(ID_OF_TEACHER)).willReturn(courses);
		
		mockMvc.perform(get("/courses/{instructorId}/offer", ID_OF_TEACHER).session(mockSession))
			.andExpect(model().attribute("isSelf", true))
			.andExpect(model().attribute("courses", courses))
			.andExpect(view().name("offered-courses"));
		then(userService).should().read(ID_OF_TEACHER);
	}
	
	@Test
	public void coursesStudentIdStudyURI_notSelf_redirect_index() 
			throws Exception {
		
		mockMvc.perform(get("/courses/{studentId}/study", ID_OTHER).session(mockSession))
			.andExpect(redirectedUrl("/"));
	}
	
	@Test
	public void coursesStudentIdStudyURI_isSelf_redirect_index() 
			throws Exception {
		
		List<Course> courses = Arrays.asList(new Course());
		
		given(courseService.coursesByStudentId(ID_OF_STUDENT)).willReturn(courses);
		
		mockMvc.perform(get("/courses/{studentId}/study", ID_OF_STUDENT).session(mockSession))
			.andExpect(model().attribute("courses", courses))
			.andExpect(view().name("studying-courses"));
	}
	
	@Test
	public void studentIdCourseIdStudyURI_success_redirect_coursesStudentIdStudy() 
			throws Exception {
		
		mockMvc.perform(
				post("/courses/{studentId}/{courseId}/study", 
						ID_OF_STUDENT,
						ID_OF_COURSE).param("keyword", KEYWORD))
			.andExpect(model().attribute("registerSuccess", true))
			.andExpect(model().attribute("keyword", KEYWORD))
			.andExpect(redirectedUrlTemplate(
					"/courses/{studentId}/study?registerSuccess=true&keyword={keyword}", 
					ID_OF_STUDENT, KEYWORD));
		
		then(courseService).should().registerCourse(ID_OF_STUDENT, ID_OF_COURSE);
	}
	
	@Test
	public void instructorIdCreateGetURI_success_return_createCourseForm() 
			throws Exception {
		
		Users user = new Users();
		user.setId(ID_OF_TEACHER);
		mockSession.setAttribute("user", user);
		
		given(courseService.isTeacher(ID_OF_TEACHER)).willReturn(true);
		
		mockMvc.perform(get("/courses/{instructorId}/create", ID_OF_TEACHER)
				.session(mockSession))
			.andExpect(model().attributeExists("title"))
			.andExpect(view().name("create-course-form"));
	}
	
	@Test
	public void instructorIdCreateGetURI_notSelfOrNotInstructor_redirect_offerCourses() 
			throws Exception {
	
		mockMvc.perform(get("/courses/{instructorId}/create", ID_OTHER)
				.session(mockSession))
			.andExpect(redirectedUrlTemplate(
					"/courses/{userId}/offer", ID_OTHER));
	}
	
	@Test
	public void instructorIdCreatePostURI_duplicateCourse_return_createCourseForm() 
			throws Exception {
	
		Course course = new Course();
		course.setId(ID_NEW);
		course.setName(ANY_NAME);
		
		given(courseService.hasDuplicateCourse(ID_OF_TEACHER, course)).willReturn(true);
		
		mockMvc.perform(post("/courses/{instructorId}/create", ID_OF_TEACHER)
				.session(mockSession)
				.flashAttr("course", course))
			.andExpect(model().attribute("duplicateCourse", is(true)))
			.andExpect(model().attribute("course", is(course)))
			.andExpect(model().attribute("instructor", is(mockSession.getAttribute("user"))))
			.andExpect(view().name("create-course-form"));
		
		then(courseService).should(never()).saveOrUpdate(course);
		assertThat(course.getName(), is(""));
	}
	
	@Test
	public void instructorIdCreatePostURI_createSuccess_redirect_offerCourses() 
			throws Exception {
	
		Course course = new Course();
		course.setId(ID_NEW);
		
		given(courseService.hasDuplicateCourse(ID_OF_TEACHER, course)).willReturn(false);
		
		mockMvc.perform(post("/courses/{instructorId}/create", ID_OF_TEACHER)
				.session(mockSession)
				.flashAttr("course", course))
			.andExpect(redirectedUrlTemplate(
					"/courses/{userId}/offer?createSuccess=true", ID_OF_TEACHER));
		
		then(courseService).should().saveOrUpdate(course);
	}
	
	@Test
	public void instructorIdCreatePostURI_updateSuccess_redirect_offerCourses() 
			throws Exception {
	
		Course course = new Course();
		course.setId(ID_OF_COURSE);
		
		mockMvc.perform(post("/courses/{instructorId}/create", ID_OF_TEACHER)
				.session(mockSession)
				.flashAttr("course", course))
			.andExpect(redirectedUrlTemplate(
					"/courses/{userId}/offer?updateSuccess=true", ID_OF_TEACHER));
		
		then(courseService).should().saveOrUpdate(course);
	}
	
	@Test
	public void instructorIdCourseIdUpdateURI_return_createCourseForm() 
			throws Exception {
	
		Course course = new Course();
		course.setId(ID_OF_COURSE);
		
		given(courseService.read(ID_OF_COURSE)).willReturn(course);
		
		mockMvc.perform(get(
				"/courses/{instructorId}/{courseId}/update", ID_OF_TEACHER, ID_OF_COURSE)
				.session(mockSession))
			.andExpect(model().attributeExists("title"))
			.andExpect(model().attribute("instructor", is(mockSession.getAttribute("user"))))
			.andExpect(model().attribute("course", course))
			.andExpect(view().name("create-course-form"));
	}
	
	@Test
	public void instructorIdCourseIdOfferDeleteURI_redirect_offerCourses() 
			throws Exception {
	
		Course course = new Course();
		course.setId(ID_OF_COURSE);
		
		mockMvc.perform(post(
				"/courses/{instructorId}/{courseId}/offer/delete", ID_OF_TEACHER, ID_OF_COURSE)
				.session(mockSession))
			.andExpect(model().attribute("instructor", is(mockSession.getAttribute("user"))))
			.andExpect(redirectedUrlTemplate(
					"/courses/{userId}/offer?deleteSuccess=true", ID_OF_TEACHER));
		then(courseService).should().delete(ID_OF_COURSE);
	}
	
	@Test
	public void search_return_searchCourses() 
			throws Exception {
		
		Course course = new Course();
		course.setId(ID_OF_COURSE);
		
		List<Course> searchCourses = Arrays.asList(course);
		
		List<Course> studyingCourses = Arrays.asList(course);
		
		List<Integer> studyingCoursesId = Arrays.asList(ID_OF_COURSE);
		
		given(courseService.coursesByKeyword(KEYWORD)).willReturn(searchCourses);
		
		given(courseService.coursesByStudentId(ID_OF_STUDENT)).willReturn(studyingCourses);
		
		mockMvc.perform(get("/courses/search")
				.param("keyword", KEYWORD)
				.session(mockSession))
			.andExpect(model().attribute("keyword", is(KEYWORD)))
			.andExpect(model().attribute("courses", is(searchCourses)))
			.andExpect(model().attribute("studyingCoursesId", is(studyingCoursesId)))
			.andExpect(view().name("search-courses"));
	}
}