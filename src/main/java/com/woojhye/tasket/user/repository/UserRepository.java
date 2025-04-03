package com.woojhye.tasket.user.repository;

import com.woojhye.tasket.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);    // 존재하면 true, 존재하지 않으면 false;
    User findByEmail(String email);
}