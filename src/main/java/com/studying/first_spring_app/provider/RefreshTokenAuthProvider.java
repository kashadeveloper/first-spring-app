package com.studying.first_spring_app.provider;

import com.studying.first_spring_app.dto.RefreshTokenAuthToken;
import com.studying.first_spring_app.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RefreshTokenAuthProvider implements AuthenticationProvider {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var refreshToken = authentication.getCredentials().toString();
        if(refreshToken.isBlank()) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        String username;
        try {
            username = jwtService.extractUsername(refreshToken);
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        var user = userDetailsService.loadUserByUsername(username);

        if(!jwtService.isTokenValid(refreshToken, user)) {
            throw new BadCredentialsException("Token expired or invalid");
        }

        return new RefreshTokenAuthToken(user, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RefreshTokenAuthToken.class.isAssignableFrom(authentication);
    }
}
