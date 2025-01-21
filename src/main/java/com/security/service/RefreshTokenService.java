package com.security.service;

import com.security.model.RefreshToken;
import com.security.repo.RefreshTokenRepo;
import com.security.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepo refreshTokenRepo;

    @Autowired
    UserRepo userRepo;

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = RefreshToken.builder()
                .users(userRepo.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(60000))  // Set expiry time, can be adjusted
                .IsLoggedOut(false)  // Initially, the user is not logged out
                .build();
        return refreshTokenRepo.save(refreshToken);
    }

    // Find refresh token by token string
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    // Verify that the token has not expired and the user is still logged in
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0 || refreshToken.isIsLoggedOut()) {
            // Token expired or user logged out, delete it and throw an exception
            refreshTokenRepo.delete(refreshToken);
            throw new RuntimeException(refreshToken.getToken() + " Refresh Token is expired or the user has logged out. Please Login again!");
        }
        return refreshToken;
    }

    // Method to handle user logout and mark refresh token as invalid
    public void logout(String token) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepo.findByToken(token);
        if (refreshTokenOpt.isPresent()) {
            RefreshToken refreshToken = refreshTokenOpt.get();
            refreshToken.setIsLoggedOut(true);  // Mark the token as logged out
            refreshTokenRepo.save(refreshToken);  // Save the change
        }
    }
}
