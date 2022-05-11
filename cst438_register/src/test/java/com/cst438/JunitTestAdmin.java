package com.cst438;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.cst438.controller.AdminController;
import com.cst438.domain.Student;
import com.cst438.domain.StudentDTO;
import com.cst438.domain.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = { AdminController.class })
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest
public class JunitTestAdmin {
	
	static final String URL = "http://localhost:8080";
	public static final String TEST_STUDENT_EMAIL = "test@csumb.edu";
	public static final String TEST_STUDENT_NAME  = "test";
	public static final String TEST_STUDENT_EMAIL_UNIQUE = "baguiar@csumb.edu";
	public static final String TEST_STATUS = "Hold";
	public static final int TEST_STATUS_CODE_HOLD = 1;
	public static final int TEST_STATUS_CODE_NO_HOLD = 0;
	
	@MockBean
	StudentRepository studentRepository;
	
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void addStudent() throws Exception {
		
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE_NO_HOLD);
		student.setStudent_id(1);
		
		given(studentRepository.findByEmail(TEST_STUDENT_EMAIL)).willReturn(student);
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.studentName = TEST_STUDENT_NAME;
		studentDTO.studentEmail = TEST_STUDENT_EMAIL_UNIQUE;
		
		response = mvc.perform(
				MockMvcRequestBuilders
				.post("/addStudent")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.id);
		
		verify(studentRepository).save(any(Student.class));
	}
	
	@Test
	public void changeHold() throws Exception {
		MockHttpServletResponse response;
		
		Student student = new Student();
		student.setEmail(TEST_STUDENT_EMAIL);
		student.setName(TEST_STUDENT_NAME);
		student.setStatusCode(TEST_STATUS_CODE_NO_HOLD);
		student.setStudent_id(1);
		
		given(studentRepository.findById(1)).willReturn(Optional.of(student));
		given(studentRepository.save(any(Student.class))).willReturn(student);
		
		StudentDTO studentDTO = new StudentDTO();
		studentDTO.id = 1;
		studentDTO.status = TEST_STATUS;
		studentDTO.statusCode = TEST_STATUS_CODE_HOLD;
		
		response = mvc.perform(
				MockMvcRequestBuilders
				.put("/hold/1")
				.content(asJsonString(studentDTO))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andReturn().getResponse();
		
		assertEquals(200, response.getStatus());
		
		StudentDTO result = fromJsonString(response.getContentAsString(), StudentDTO.class);
		assertNotEquals( 0  , result.id);
		
		verify(studentRepository).save(any(Student.class));
	}
	
	private static String asJsonString(final Object obj) {
		try {

			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static <T> T  fromJsonString(String str, Class<T> valueType ) {
		try {
			return new ObjectMapper().readValue(str, valueType);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}