package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import com.cst438.domain.Admin;
import com.cst438.domain.AdminRepository;
import com.cst438.domain.Student;
import com.cst438.domain.StudentRepository;


@Controller
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {
	/*
	 * user by React Login front end component to test if user is 
	 * logged in.
	 * response 401 indicates user is not logged in
	 * a redirect response take user to Semester front end page.
	 */
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	AdminRepository adminRepository;
	
	
	@Value("${frontend.post.login.url}")
	String redirect_url;
	
	@GetMapping("/user")
	public String user (@AuthenticationPrincipal OAuth2User principal) {
		// used by front end to display user name.
		return "redirect:" + redirect_url;
	}
	
	@GetMapping("/authenticate")
	public String user_admin (@AuthenticationPrincipal OAuth2User principal) {
		String email = principal.getAttribute("email");
		
		Admin admin = adminRepository.findByEmail(email);
		
		if(admin != null)
			return "admin";
		
		Student student = studentRepository.findByEmail(email);
		
		if(student != null)
			return "student";
		
		throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid Credentials " );
	}
}