package com.security.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.Student;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class StudentController {
	
	private List<Student> students = new ArrayList<>(List.of(
			new Student(1, "Vaishnavi", 99),
			new Student(2, "Samiksha", 90)));
	
	@GetMapping("/students")
	public List<Student> getStudents(){
		return students;
		
	}
	
	@PostMapping("/addStudent")
	public Student addStudent(@RequestBody Student student) {
		students.add(student);
		return student;
	}
	
	@GetMapping("csrf-Token")
	public CsrfToken getcsrfToken(HttpServletRequest request) {
		return (CsrfToken) request.getAttribute("_csrf");
		
	}
	

}
