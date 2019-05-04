package com.wei.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
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

import com.wei.entity.Course;
import com.wei.entity.Users;
import com.wei.service.CourseService;
import com.wei.service.UserService;

@Controller
@RequestMapping("/courses")
public class CourseController {
	
	private final String SHOW_OFFERED_COURSE = "redirect:/courses/%d/offer";
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/{instructorId}/offer")
	public String offeredCourses(@PathVariable int instructorId, 
			HttpSession session, Model theModel) {
		
		Users user = (Users)session.getAttribute("user");
		
		Users instructor = userService.read(instructorId);
		
		theModel.addAttribute("instructor", instructor);
		
		if(isSelf(user, instructorId) && isTeacher(instructorId)) {
			theModel.addAttribute("isSelf", true);
		}
		
		List<Course> courses = courseService.coursesByInstructorId(instructorId);
		
		if(!courses.isEmpty())
			theModel.addAttribute("courses", courses);
		
		return "offered-courses";
	}
	
	@GetMapping("/{instructorId}/create")
	public String showCreateCourse(@PathVariable int instructorId, 
			HttpSession session, Model theModel) {
		
		Users user = (Users)session.getAttribute("user");
		
		if(isSelf(user, instructorId) && isTeacher(instructorId)) {
			
			initCreateCourseForm(user, theModel);
			
			return "create-course-form";
		}
		
		return String.format(SHOW_OFFERED_COURSE, instructorId);
	}
	
	@PostMapping("/{instructorId}/create")
	public String createProcess(
			@Valid @ModelAttribute("course") Course course,
			BindingResult bindingResult,
			@PathVariable int instructorId,
			HttpSession session, 
			Model theModel) {
		
		Users user = (Users)session.getAttribute("user");
		
		if(bindingResult.hasErrors()) {
			initCreateCourseForm(user, theModel);
			return "create-course-form";
		}
		
		if(isSelf(user, instructorId) && 
		   isTeacher(instructorId)) {
			
			if(courseService.hasDuplicateCourse(instructorId, course)) {		
				theModel.addAttribute("duplicateCourse", true);
				course.setName("");
				theModel.addAttribute("course", course);
				theModel.addAttribute("instructor", user);
				return "create-course-form";
			} else {
				int courseId = course.getId();
				boolean isUpdate = false;
				initCreateCourseForm(user, theModel);
				
				if(courseId != 0) {
					isUpdate = true;
					course = courseService.read(courseId);
				}
				
				courseService.saveOrUpdate(course);
				if(isUpdate) {
					theModel.addAttribute("updateSuccess", true);
				} else {
					theModel.addAttribute("createSuccess", true);
				}
			}
		}
		
		return String.format(SHOW_OFFERED_COURSE, instructorId);
	}
	
	@GetMapping("/{instructorId}/{courseId}/update")
	public String updateCourse(
			@PathVariable int instructorId, 
			@PathVariable int courseId,
			HttpSession session, Model theModel) {
		
		Users user = (Users)session.getAttribute("user");
		
		if(isSelf(user, instructorId) && isTeacher(instructorId)) {
			
			theModel.addAttribute("instructor", user);
			
			theModel.addAttribute("course", courseService.read(courseId));
			
			return "create-course-form";
		}
		
		return String.format(SHOW_OFFERED_COURSE, instructorId);
	}
	
	@GetMapping("/{instructorId}/{courseId}/delete")
	public String deleteCourse(
			@PathVariable int instructorId, 
			@PathVariable int courseId,
			HttpSession session, Model theModel) {
		
		Users user = (Users)session.getAttribute("user");
		
		if(isSelf(user, instructorId) && isTeacher(instructorId)) {
			
			theModel.addAttribute("instructor", user);
			
			theModel.addAttribute("deleteSuccess", true);
			
			courseService.delete(courseId);
		}
		
		
		return String.format(SHOW_OFFERED_COURSE, instructorId);
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
