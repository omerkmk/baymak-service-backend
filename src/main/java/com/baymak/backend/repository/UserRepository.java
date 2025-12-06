package com.baymak.backend.repository;

import com.baymak.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Ekstra sorgular:
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
