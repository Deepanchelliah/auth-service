package com.deepan.auth_service.util;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class MyJwtAuthToken extends AbstractAuthenticationToken {

    private final Object principal;
    private Object credentials;

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public MyJwtAuthToken(String username, String password){
        super(null);
        this.principal = username;
        this.credentials = password;
        setAuthenticated(false);
    }

    public MyJwtAuthToken(Object principal, Object credentials,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(true);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
