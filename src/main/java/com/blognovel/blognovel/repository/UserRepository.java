package com.blognovel.blognovel.repository;

import com.blognovel.blognovel.enums.Role;
import com.blognovel.blognovel.enums.Status;
import com.blognovel.blognovel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    long countByStatus(Status status);

    long countByRole(Role role);
}
