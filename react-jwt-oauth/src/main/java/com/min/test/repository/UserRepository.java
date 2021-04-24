package com.min.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.min.test.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByUsername(String username);
}
