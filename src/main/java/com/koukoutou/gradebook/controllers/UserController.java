package com.koukoutou.gradebook.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.koukoutou.gradebook.models.User;
import com.koukoutou.gradebook.repositories.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

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

		return "index";
	}

	@PostMapping("/logout")
	public String logoutUser() {

		return "fragments/login_form";
	}

	@GetMapping("/login-error")
	public String showLoginError(Model model) {

		return "fragments/login_error";
	}

	@GetMapping("/account")
	public String showAccountInfo(@AuthenticationPrincipal User user, Model model) {

		model.addAttribute("user", user);

		return "fragments/account";
	}

	@PostMapping("/account")
	public String updateAccountInfo(@Valid User user, Errors errors, Model model) {

		if (errors != null && errors.getErrorCount() > 0) {
			return "fragments/account";
		} else {
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			userRepository.save(user);
		}

		return "redirect:/account";
	}
}
