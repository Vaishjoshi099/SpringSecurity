package com.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.model.Users;
import com.security.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	public UserService userService;
	
	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		return userService.register(user);
	}
	
	@GetMapping("/users")
	public List<Users> getUsers(){
		return userService.getAllUsers();
	}
	
	
	@PostMapping("/login")
	public String login(@RequestBody Users user) {
		System.out.println("Success");
		return userService.verify(user);
	}
	
	

}
