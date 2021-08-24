package com.ymh.springsecuritystudy.service;

import com.ymh.springsecuritystudy.domain.Role;
import com.ymh.springsecuritystudy.domain.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    Role saveRole(Role role);

    void addRoleToUser(String username, String roleName);

    User getUser(String username);

    List<User> getUsers();
}
