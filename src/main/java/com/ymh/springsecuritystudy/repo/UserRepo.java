package com.ymh.springsecuritystudy.repo;

import com.ymh.springsecuritystudy.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
