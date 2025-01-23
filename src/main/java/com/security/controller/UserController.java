package com.security.controller;

import java.util.List;

import com.security.DTO.AuthRequestDTO;
import com.security.DTO.JwtResponseDTO;
import com.security.DTO.RefreshTokenDTO;
import com.security.model.RefreshToken;
import com.security.service.JWTService;
import com.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.security.model.Users;
import com.security.service.UserService;

@RestController
public class UserController {

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	public UserService userService;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Autowired
	JWTService jwtService;
	
	@PostMapping("/register")
	public Users register(@RequestBody Users user) {
		return userService.register(user);
	}
	
	@GetMapping("/users")
	public List<Users> getUsers(){
		return userService.getAllUsers();
	}
	
	
//	@PostMapping("/login")
//	public String login(@RequestBody Users user) {
//		System.out.println("Success");
//		return userService.verify(user);
//	}

	@PostMapping("/login")
	public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),authRequestDTO.getPassword()));
		if(authentication.isAuthenticated()){
			RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
			return JwtResponseDTO.builder().
					accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
					.token(refreshToken.getToken())
					.build();
		}
		else{
			throw new UsernameNotFoundException("Invalid user request!!");
		}
	}

	@PostMapping("/refreshToken")
	public JwtResponseDTO refreshToken(@RequestBody RefreshTokenDTO refreshTokenDTO){
		return refreshTokenService.findByToken(refreshTokenDTO.getToken())
				.map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUsers)
				.map(users -> {
					String accessToken = jwtService.generateToken(users.getUsername());
					return JwtResponseDTO.builder()
							.accessToken(accessToken)
							.token(refreshTokenDTO.getToken()).build();
				}).orElseThrow(()->new RuntimeException("Refresh Token is not in DB"));
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		String refreshToken = userService.getRefreshTokenForUser(username);

		if (refreshToken != null) {
			try {
				refreshTokenService.logout(refreshToken);
				// Explicit success response with content
				return ResponseEntity.ok("Successfully logged out");
			} catch (RuntimeException e) {
				// Explicit error response with content
				return ResponseEntity.status(400).body("Logout failed: " + e.getMessage());
			}
		} else {
			// Explicit response when no active session is found
			return ResponseEntity.status(400).body("No active session found for the user.");
		}
	}

}
