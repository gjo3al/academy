package com.wei.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.wei.entity.Course;
import com.wei.entity.Users;
import com.wei.service.CourseService;
import com.wei.service.UserService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	private final String SHOW_OFFERED_COURSE = "redirect:/courses/%d/offer";
	
	private final String SHOW_STUDYING_COURSE = "redirect:/courses/%d/study";
	
	private final String CREATE_COURSE_TITLE = "新增課程";
	
	private final String UPDATE_COURSE_TITLE = "更新課程";
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{instructorId}/offer")
	public String offeredCourses(
			@PathVariable int instructorId, 
			@SessionAttribute("user") Users user, 
			Model theModel) {
		
		Users instructor = userService.read(instructorId);
		
		if(instructor == null || !isTeacher(instructorId)) {
			return "offered-courses";
		}
		
		theModel.addAttribute("instructor", instructor);
		
		if(isSelf(user, instructorId) && isTeacher(instructorId)) {
			theModel.addAttribute("isSelf", true);
		}
		
		List<Course> courses = courseService.coursesByInstructorId(instructorId);
		
		if(!courses.isEmpty())
			theModel.addAttribute("courses", courses);
		
		return "offered-courses";
	}
	
	@GetMapping("/{studentId}/study")
	public String studyingCourses(
			@PathVariable int studentId, 
			@SessionAttribute("user") Users user, 
			Model theModel) {
		
		if(!isSelf(user, studentId)) {
			return "redirect:/";
		}
		
		List<Course> courses = courseService.coursesByStudentId(studentId);
		
		if(!courses.isEmpty())
			theModel.addAttribute("courses", courses);
		
		return "studying-courses";
	}
	
	@PostMapping("/{studentId}/{courseId}/study")
	public String registerCourse(
			@RequestParam String keyword,
			@PathVariable int studentId,
			@PathVariable int courseId,
			Model theModel) {
		
		courseService.registerCourse(studentId, courseId);
		
		theModel.addAttribute("registerSuccess", true);
		
		theModel.addAttribute("keyword", keyword);
		
		return String.format(SHOW_STUDYING_COURSE, studentId);
	}
	
	@GetMapping("/{instructorId}/create")
	public String showCreateCourse(
			@PathVariable int instructorId, 
			@SessionAttribute("user") Users user, 
			Model theModel) {
		
		if(isSelf(user, instructorId) && isTeacher(instructorId)) {
			
			initCreateCourseForm(user, theModel);
			
			theModel.addAttribute("title", CREATE_COURSE_TITLE);
			
			return "create-course-form";
		}
		
		return String.format(SHOW_OFFERED_COURSE, instructorId);
	}
	
	@PostMapping("/{instructorId}/create")
	public String createProcess(
			@Valid @ModelAttribute("course") Course course,
			BindingResult bindingResult,
			@PathVariable int instructorId,
			@SessionAttribute("user") Users user, 
			Model theModel) {
		
		if(bindingResult.hasErrors()) {
			initCreateCourseForm(user, theModel);
			return "create-course-form";
		}
			
		if(courseService.hasDuplicateCourse(instructorId, course)) {		
			theModel.addAttribute("duplicateCourse", true);
			course.setName("");
			theModel.addAttribute("course", course);
			theModel.addAttribute("instructor", user);
			return "create-course-form";
		} else {
			int courseId = course.getId();
			initCreateCourseForm(user, theModel);
			
			courseService.saveOrUpdate(course);
			if(courseId != 0) {
				theModel.addAttribute("updateSuccess", true);
			} else {
				theModel.addAttribute("createSuccess", true);
			}
		}
		
		return String.format(SHOW_OFFERED_COURSE, instructorId);
	}
	
	@GetMapping("/{instructorId}/{courseId}/update")
	public String updateCourse(
			@PathVariable int instructorId, 
			@PathVariable int courseId,
			@SessionAttribute("user") Users user, 
			Model theModel) {
			
		theModel.addAttribute("instructor", user);
		
		theModel.addAttribute("course", courseService.read(courseId));
		
		theModel.addAttribute("title", UPDATE_COURSE_TITLE);
			
		return "create-course-form";

	}
	
	@PostMapping("/{instructorId}/{courseId}/offer/delete")
	public String deleteOfferedCourse(
			@PathVariable int instructorId, 
			@PathVariable int courseId,
			@SessionAttribute("user") Users user, 
			Model theModel) {
		
		theModel.addAttribute("instructor", user);
			
		theModel.addAttribute("deleteSuccess", true);
			
		courseService.delete(courseId);

		return String.format(SHOW_OFFERED_COURSE, instructorId);
	}
	
	@PostMapping("/{studentId}/{courseId}/study/delete")
	public String deleteStudyingCourse(
			@RequestParam String keyword,
			@PathVariable int studentId, 
			@PathVariable int courseId,
			Model theModel) {
			
		theModel.addAttribute("deleteSuccess", true);
		
		theModel.addAttribute("keyword", keyword);
		
		courseService.deleteStudyingCourse(studentId, courseId);
		
		return String.format(SHOW_STUDYING_COURSE, studentId);
	}
	
	@GetMapping("/search")
	public String coursesByKeyword(
			@RequestParam("keyword") String keyword,
			@SessionAttribute("user") Users user,
			Model theModel) {
		
		List<Course> courses = courseService.coursesByKeyword(keyword);
		
		List<Integer> studyingCoursesId = 
				courseService.coursesByStudentId(user.getId()).stream()
				.map(Course::getId)
				.collect(Collectors.toList());
		
		theModel.addAttribute("keyword", keyword);
		
		theModel.addAttribute("courses", courses);
		
		theModel.addAttribute("studyingCoursesId", studyingCoursesId);
		
		return "search-courses";
	}
	
	private boolean isTeacher(int instructorId) {
		
		return courseService.isTeacher(instructorId);
		
	}
	
	private boolean isSelf(Users user, int id) {
		return user.getId() == id;
	}
	
	private void initCreateCourseForm(Users instructor, Model theModel) {
		theModel.addAttribute("instructor", instructor);
		
		// BindingResult is attached to Course object 
		if(!theModel.containsAttribute("course"))
			theModel.addAttribute("course", new Course());
	}
}
