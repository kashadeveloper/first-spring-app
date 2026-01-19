package com.studying.first_spring_app.service;

import com.studying.first_spring_app.config.JwtProperties;
import com.studying.first_spring_app.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {
    static String secret = "c2VsZWN0IGFnZSwgbmFtZSBmcm9tIHVzZXJzIHdoZXJlIGlkID0gMQ==";
    @InjectMocks
    JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setPrivateKey(secret);
        jwtService = new JwtService(jwtProperties);
    }

    @Test
    void shouldGenerateValidAccessToken() {
        var user = User.builder().username("Lolo").password("Password").build();

        var token = jwtService.generateToken(user);

        assertNotNull(token, "token should not be null");

        var claims = Jwts.parser()
                .verifyWith(getPrivateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertNotNull(claims, "claims should not be null");
        assertNotNull(claims.getSubject(), "subject should not be null");
        assertEquals("Lolo", claims.getSubject(), "subject should be Lolo");
        assertTrue(claims.getExpiration().after(new Date(System.currentTimeMillis())), "expiration should be after now");
    }

    @Test
    void shouldGenerateValidRefreshToken() {
        var user = User.builder().username("Lolo").password("Password").build();

        var token = jwtService.generateRefreshToken(user);

        assertNotNull(token, "token should not be null");

        var claims = Jwts.parser()
                .verifyWith(getPrivateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertNotNull(claims, "claims should not be null");
        assertNotNull(claims.getSubject(), "subject should not be null");
        assertEquals("Lolo", claims.getSubject(), "subject should be Lolo");
        assertTrue(claims.getExpiration().after(new Date(System.currentTimeMillis())), "expiration should be after now");
    }

    private SecretKey getPrivateKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
