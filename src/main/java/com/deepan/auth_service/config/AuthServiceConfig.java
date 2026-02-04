package com.deepan.auth_service.config;

import com.deepan.auth_service.service.TokenService;
import com.deepan.auth_service.util.MyAuthenticationProvider;
import com.deepan.auth_service.util.MyCustomFilter;
import com.deepan.auth_service.util.MyJwtAuthToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AuthServiceConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager, TokenService tokenService) throws Exception {

        MyCustomFilter myCustomFilter = new MyCustomFilter(authenticationManager,tokenService);

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/token").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())
                )
                .addFilterAt(myCustomFilter, UsernamePasswordAuthenticationFilter.class);
                //.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    MyAuthenticationProvider myAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        return new MyAuthenticationProvider(userDetailsService, passwordEncoder);
    }

    @Bean
    public AuthenticationManager authenticationManager(MyAuthenticationProvider myAuthenticationProvider) {
        return new ProviderManager(List.of(myAuthenticationProvider));
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
