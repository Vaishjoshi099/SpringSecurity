package com.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.security.model.Users;

public interface UserRepo extends JpaRepository<Users, Integer>{
	Users findByUsername(String username);

}
