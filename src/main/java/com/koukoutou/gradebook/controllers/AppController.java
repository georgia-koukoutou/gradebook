package com.koukoutou.gradebook.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.koukoutou.gradebook.models.Course;
import com.koukoutou.gradebook.models.CourseGrade;
import com.koukoutou.gradebook.models.User;
import com.koukoutou.gradebook.repositories.CourseGradeRepository;
import com.koukoutou.gradebook.repositories.CourseRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AppController {

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private CourseGradeRepository courseGradeRepository;

	@GetMapping("/")
	public String index(Model model) {

		List<Course> courses = courseRepository.findAll();
		model.addAttribute("courses", courses);

		return "fragments/index";
	}

	@Transactional
	@PostMapping("/")
	public String addNewCourses(@AuthenticationPrincipal User user, HttpServletRequest request) {

		String[] courseIds = request.getParameterValues("course_id");
		String[] grades = request.getParameterValues("course_grade");

		if (courseIds != null && courseIds.length > 0 && grades != null && grades.length > 0) {

			for (int i = 0; i < courseIds.length; i++) {

				Course course = courseRepository.findById(Long.parseLong(courseIds[i])).get();
				double grade = Double.parseDouble(grades[i]);

				log.info("Course: {} Grade: {} - {}", courseIds[i], grades[i], course);

				CourseGrade courseGrade = new CourseGrade();
				courseGrade.setUser(user);
				courseGrade.setCourse(course);
				courseGrade.setGrade(grade);

				courseGradeRepository.save(courseGrade);
			}
		}

		return "redirect:/";
	}
}