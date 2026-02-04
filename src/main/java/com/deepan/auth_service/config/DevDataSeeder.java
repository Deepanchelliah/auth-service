package com.deepan.auth_service.config;

import com.deepan.auth_service.entity.RoleEntity;
import com.deepan.auth_service.entity.UserEntity;
import com.deepan.auth_service.repository.RoleRepository;
import com.deepan.auth_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DevDataSeeder {

    @Bean
    CommandLineRunner seed(UserRepository users, RoleRepository roles, PasswordEncoder encoder) {
        return args -> {
            RoleEntity userRole = roles.findByName("ROLE_USER").orElseGet(() -> {
                RoleEntity r = new RoleEntity();
                r.setName("ROLE_USER");
                return roles.save(r);
            });

            users.findByUsername("deepan").orElseGet(() -> {
                UserEntity u = new UserEntity();
                u.setUsername("deepan");
                u.setPasswordHash(encoder.encode("chelliah"));
                u.getRoles().add(userRole);
                return users.save(u);
            });
        };
    }
}