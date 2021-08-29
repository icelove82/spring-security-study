package com.ymh.springsecuritystudy;

import com.ymh.springsecuritystudy.domain.Role;
import com.ymh.springsecuritystudy.domain.User;
import com.ymh.springsecuritystudy.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringSecurityStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityStudyApplication.class, args);
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            userService.saveRole(Role.builder().name("ROLE_USER").build());
            userService.saveRole(Role.builder().name("ROLE_MANGER").build());
            userService.saveRole(Role.builder().name("ROLE_ADMIN").build());
            userService.saveRole(Role.builder().name("ROLE_SUPER_ADMIN").build());

            userService.saveUser(User.builder()
                    .name("John Travolta")
                    .username("john")
                    .password("1234").build());

            userService.saveUser(User.builder()
                    .name("Will Smith")
                    .username("will")
                    .password("1234").build());

            userService.saveUser(User.builder()
                    .name("Jim Carry")
                    .username("jim")
                    .password("1234").build());

            userService.saveUser(User.builder()
                    .name("Arnold Schwarzenegger")
                    .username("arnold")
                    .password("1234").build());

            userService.addRoleToUser("john", "ROLE_USER");
            userService.addRoleToUser("john", "ROLE_MANGER");
            userService.addRoleToUser("will", "ROLE_MANGER");
            userService.addRoleToUser("jim", "ROLE_ADMIN");
            userService.addRoleToUser("arnold", "ROLE_SUPER_ADMIN");
            userService.addRoleToUser("arnold", "ROLE_ADMIN");
            userService.addRoleToUser("arnold", "ROLE_USER");
        };
    }

}
