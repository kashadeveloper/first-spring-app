package com.studying.first_spring_app.dto;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class RefreshTokenAuthToken extends AbstractAuthenticationToken {
    private final String refreshToken;
    private final UserDetails user;

    public RefreshTokenAuthToken(String refreshToken) {
        super(Collections.emptyList());
        this.refreshToken = refreshToken;
        this.user = null;
        setAuthenticated(false);
    }
    public RefreshTokenAuthToken(UserDetails principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        refreshToken = null;
        user = principal;
        setAuthenticated(true);
    }

    @Override
    public @Nullable String getCredentials() {
        return refreshToken;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return user;
    }
}
