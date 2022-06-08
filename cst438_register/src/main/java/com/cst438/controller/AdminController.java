package com.cst438.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.cst438.domain.Admin;
import com.cst438.domain.AdminRepository;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
	@Autowired
	StudentRepository studentRepository;
	@Autowired
	AdminRepository adminRepository;
	
	/*
	 * Add student to database
	 * Fulfills As an administrator, I can add a student to the system.
	 * I input the student email and name.  
	 * The student email must not already exists in the system.
	 */
	
	@PostMapping("/addStudent")
	@Transactional
	public StudentDTO addStudent (@RequestBody StudentDTO studentDTO, @AuthenticationPrincipal OAuth2User principal) {
		String admin_email = principal.getAttribute("email");
		
		Admin admin = adminRepository.findByEmail(admin_email);
		if(admin == null)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid admin access");
		}
		
		Student tempstudent = new Student();
		tempstudent = studentRepository.findByEmail(studentDTO.studentEmail);
		
		if(tempstudent == null && studentDTO.studentName != null && studentDTO.studentEmail != null) {
			Student student = new Student();
			student.setName(studentDTO.studentName);
			student.setEmail(studentDTO.studentEmail);
			student.setStatus(studentDTO.status);
			student.setStatusCode(studentDTO.statusCode);
			Student savedStudent = studentRepository.save(student);
			
			StudentDTO result = createStudentDTO(savedStudent);
			return result;
			
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid student or email address.  ");
		}
	}
	
	/*
	 * Add and Remove Hold
	 * Fulfills: As an administrator, I can put student registration on HOLD.
	 * As an administrator, I can release the HOLD on student registration.
	 */
	@PutMapping("/hold/{student_id}")
	public StudentDTO changeHold(@RequestBody StudentDTO studentDTO, @PathVariable("student_id") int student_id, @AuthenticationPrincipal OAuth2User principal) {
		String admin_email = principal.getAttribute("email");
		
		Admin admin = adminRepository.findByEmail(admin_email);
		if(admin == null)
		{
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid admin access");
		}
		Student student = studentRepository.findById(student_id).orElse(null);
		
		if (student != null) {
			student.setStatus(studentDTO.status);
			student.setStatusCode(studentDTO.statusCode);
			Student savedStudent = studentRepository.save(student);
			
			StudentDTO result = createStudentDTO(savedStudent);
			return result;
		} else {
			throw  new ResponseStatusException( HttpStatus.BAD_REQUEST, "Invalid student or email address.  ");
		}
	}
	
	private StudentDTO createStudentDTO(Student student) {
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.id = student.getStudent_id();
		studentDTO.studentName = student.getName();
		studentDTO.studentEmail = student.getEmail();
		studentDTO.status = student.getStatus();
		studentDTO.statusCode = student.getStatusCode();
		return studentDTO;
	}
}
