package com.ymh.springsecuritystudy.service;

import com.ymh.springsecuritystudy.domain.Role;
import com.ymh.springsecuritystudy.domain.User;
import com.ymh.springsecuritystudy.repo.RoleRepo;
import com.ymh.springsecuritystudy.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User user) {
        log.info("Saving new user {} to the db", user.getName());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    public Role saveRole(Role role) {
        log.info("Saving new role {} to the db", role.getName());
        return roleRepo.save(role);
    }

    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to the user {}", roleName, username);
        User user = userRepo.findByUsername(username);
        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    public User getUser(String username) {
        log.info("Fetching user {}", username);
        return userRepo.findByUsername(username);
    }

    public List<User> getUsers() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            log.error("User not found in db");
            throw new UsernameNotFoundException("User not found in db");
        } else {
            log.info("User found in db: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities =
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities);
    }
}
