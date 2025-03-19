package com.example.learnjava.springmvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.learnjava.springmvc.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
