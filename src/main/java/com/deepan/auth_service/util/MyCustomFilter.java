package com.deepan.auth_service.util;

import com.deepan.auth_service.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

public class MyCustomFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenService tokenService;

    public MyCustomFilter(AuthenticationManager authenticationManager, TokenService tokenService) {
        super(new AntPathRequestMatcher("/token", "POST"));
        setAuthenticationManager(authenticationManager);
        this.tokenService = tokenService;
    }

    record LoginRequest(String username, String password) {}

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {

        String ct = request.getContentType();
        if (ct == null || !ct.toLowerCase().startsWith(MediaType.APPLICATION_JSON_VALUE)) {
            throw new RuntimeException("Content-Type must be application/json");
        }

        LoginRequest body = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        var authRequest = new MyJwtAuthToken(body.username(), body.password());
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {

        String jwt = tokenService.generateToken(authResult);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getOutputStream(), Map.of(
                "token_type", "Bearer",
                "access_token", jwt
        ));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        objectMapper.writeValue(response.getOutputStream(), Map.of(
                "error", "unauthorized",
                "message", failed.getMessage()
        ));
    }
}
