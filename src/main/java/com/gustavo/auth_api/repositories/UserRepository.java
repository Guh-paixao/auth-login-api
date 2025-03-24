package com.gustavo.auth_api.repositories;

import com.gustavo.auth_api.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
