package com.security.repo;

import com.security.model.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends CrudRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUsers_Username(String username);

}
