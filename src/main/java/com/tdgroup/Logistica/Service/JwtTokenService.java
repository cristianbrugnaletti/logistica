package com.tdgroup.Logistica.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class JwtTokenService {
/*
    @Value("${jwt.expiration}")
    private Long expiration;

    private final SecretKey secretKey;

    public JwtTokenService(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    */

/*    public String generateToken(String username, String tipoUtente) {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(expiration, ChronoUnit.MILLIS);

        return Jwts.builder()
                .setSubject(username)
                .claim("tipoUtente", tipoUtente)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiryInstant))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    */
}