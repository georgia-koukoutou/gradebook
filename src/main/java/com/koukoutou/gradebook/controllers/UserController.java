package com.koukoutou.gradebook.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.koukoutou.gradebook.models.CourseGrade;
import com.koukoutou.gradebook.models.User;
import com.koukoutou.gradebook.repositories.CourseGradeRepository;
import com.koukoutou.gradebook.repositories.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CourseGradeRepository courseGradeRepository;

	@GetMapping("/register")
	public String showRegistrationForm(Model model) {

		User user = new User();
		model.addAttribute("user", user);

		return "fragments/register_form";
	}

	@PostMapping("/register")
	public String registerUser(@Valid User newUser, Errors errors, Model model) {

		if (errors != null && errors.getErrorCount() > 0) {
			return "fragments/register_form";
		} else {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(newUser.getPassword());
			newUser.setPassword(encodedPassword);

			userRepository.save(newUser);
		}

		return "fragments/login_form";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {

		User user = new User();
		model.addAttribute("user", user);

		return "fragments/login_form";
	}

	@PostMapping("/login")
	public String loginUser() {

		return "layout";
	}

	@PostMapping("/logout")
	public String logoutUser() {

		return "fragments/login_form";
	}

	@GetMapping("/login-error")
	public String showLoginError(Model model) {

		return "fragments/login_error";
	}

	@GetMapping("/courses")
	public String showGrades(@AuthenticationPrincipal User user, Model model) {

		List<CourseGrade> userCourseGrades = courseGradeRepository.findByUserId(user.getId());
		model.addAttribute("courseGrades", userCourseGrades);
		System.out.println(userCourseGrades.toString());

		return "fragments/courses";
	}

	@Transactional
	@PostMapping("/delete")
	public String deleteCourse(@AuthenticationPrincipal User user, @RequestParam("id") Long id) {

		courseGradeRepository.deleteByCourseIdAndUserId(id, user.getId());

		return "redirect:/courses";
	}

}
