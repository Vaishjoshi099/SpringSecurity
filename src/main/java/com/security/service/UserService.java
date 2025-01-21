package com.security.service;

import java.util.List;

import com.security.repo.RefreshTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.model.Users;
import com.security.repo.UserRepo;

@Service
public class UserService {
	
	@Autowired
	AuthenticationManager authManager ;
	
	@Autowired
	public UserRepo urepo;

	@Autowired
	public RefreshTokenRepo refreshTokenRepo;
	
	@Autowired
	private JWTService jwtService;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public Users register(Users user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return urepo.save(user);
	}
	
	public List<Users> getAllUsers(){
		return urepo.findAll();
	}

	public String verify(Users user) {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if(authentication.isAuthenticated()) {
		return jwtService.generateToken(user.getUsername());
		}
		return "failure";
	}
	public String getRefreshTokenForUser(String username) {
		// Fetch the refresh token associated with the username from the database
		return refreshTokenRepo.findByUsers_Username(username)
				.map(refreshToken -> refreshToken.getToken())  // Assuming your RefreshToken entity has a 'getToken' method
				.orElse(null);
	}

}
