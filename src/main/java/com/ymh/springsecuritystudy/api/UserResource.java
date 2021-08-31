package com.ymh.springsecuritystudy.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ymh.springsecuritystudy.domain.Role;
import com.ymh.springsecuritystudy.domain.User;
import com.ymh.springsecuritystudy.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping("users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("user/save")
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/api/user/save")
                        .toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath().path("/api/role/save")
                        .toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("role/addtouser")
    public ResponseEntity<?> addToUser(@RequestBody RoleToUserForm form) {
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);

                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);

                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toList()))
                        .sign(algorithm);

                response.addHeader("access_token", access_token);
                response.addHeader("refresh_token", refresh_token);
            } catch (Exception e) {
                response.addHeader("ERROR", e.getMessage());
                response.setStatus(FORBIDDEN.value());
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}

@Data
class RoleToUserForm {
    private String username;
    private String roleName;
}

