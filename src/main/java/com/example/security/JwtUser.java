package com.example.security;

import lombok.Getter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Map;

@Getter
public class JwtUser extends Jwt {
    private Long userId;
    private String email;

    public JwtUser(String tokenValue, Instant issuedAt, Instant expiresAt, Map<String, Object> headers, Map<String, Object> claims) {
        super(tokenValue, issuedAt, expiresAt, headers, claims);
    }

    public JwtUser(Jwt jwt, Long userId, String email) {
        super(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getHeaders(), jwt.getClaims());
        this.userId = userId;
        this.email = email;
    }
}
