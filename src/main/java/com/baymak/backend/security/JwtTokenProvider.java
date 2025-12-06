package com.baymak.backend.security;

import com.baymak.backend.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSigningKey() {
        // HMAC-SHA256 için minimum 32 byte (256 bit) gereklidir
        // Daha güvenli olması için 64 byte (512 bit) önerilir
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        
        // Eğer key çok kısa ise, SHA-256 hash ile genişlet
        if (keyBytes.length < 32) {
            log.warn("JWT secret key is too short ({} bytes). Minimum 32 bytes required for HMAC-SHA256.", keyBytes.length);
            // Key'i SHA-256 ile hash'leyerek 32 byte'a çıkar
            java.security.MessageDigest digest = null;
            try {
                digest = java.security.MessageDigest.getInstance("SHA-256");
                keyBytes = digest.digest(keyBytes);
            } catch (java.security.NoSuchAlgorithmException e) {
                log.error("SHA-256 algorithm not available", e);
                throw new RuntimeException("Failed to process JWT secret key", e);
            }
        }
        
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);

        try {
            return Jwts.builder()
                    .subject(user.getEmail())
                    .claim("role", user.getRole().name())
                    .issuedAt(now)
                    .expiration(expiryDate)
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            log.error("Error generating JWT token for user: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public boolean validateToken(String token) {
        try {
            if (token == null || token.isEmpty()) {
                log.warn("JWT token is null or empty");
                return false;
            }
            
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            log.debug("JWT token validated successfully");
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired: {}", e.getMessage());
            return false;
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {} - Error: {}", token.substring(0, Math.min(20, token.length())) + "...", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error validating JWT token", e);
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired while extracting email");
            throw new RuntimeException("JWT token has expired", e);
        } catch (JwtException e) {
            log.error("Invalid JWT token while extracting email: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        } catch (Exception e) {
            log.error("Unexpected error extracting email from JWT token", e);
            throw new RuntimeException("Failed to extract email from token", e);
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("role", String.class);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired while extracting role");
            throw new RuntimeException("JWT token has expired", e);
        } catch (JwtException e) {
            log.error("Invalid JWT token while extracting role: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        } catch (Exception e) {
            log.error("Unexpected error extracting role from JWT token", e);
            throw new RuntimeException("Failed to extract role from token", e);
        }
    }
}

