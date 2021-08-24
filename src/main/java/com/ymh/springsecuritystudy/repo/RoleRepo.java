package com.ymh.springsecuritystudy.repo;

import com.ymh.springsecuritystudy.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}