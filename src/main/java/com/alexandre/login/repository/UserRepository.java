package com.alexandre.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alexandre.login.form.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);    
}
