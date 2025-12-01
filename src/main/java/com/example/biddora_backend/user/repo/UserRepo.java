package com.example.biddora_backend.user.repo;

import com.example.biddora_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
