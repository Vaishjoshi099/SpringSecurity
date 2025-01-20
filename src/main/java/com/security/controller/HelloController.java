package com.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class HelloController {
	
	
	//CSRF-Cross Site Request Forgery
	@GetMapping("/")
	public String greet(HttpServletRequest request) {
		return "Welcome to Home Page" + request.getSession().getId()  ;
	}

}
