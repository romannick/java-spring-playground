package com.nikolayr.playground.userService.util.auth;

import com.nikolayr.playground.shared.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;
    private final Key key;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.access.expiration.ms}") long accessTokenExpirationMs,
                   @Value("${jwt.refresh.expiration.ms}") long refreshTokenExpirationMs
                   ) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.accessTokenExpirationMs = accessTokenExpirationMs;
        this.refreshTokenExpirationMs = refreshTokenExpirationMs;
    }

    public String generateAccessToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.accessTokenExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + this.refreshTokenExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public Mono<Void> validateToken(String token) {
        return Mono.fromRunnable(() -> {
            try {
                Jwts.parserBuilder()
                        .setSigningKey(this.key)
                        .build()
                        .parseClaimsJws(token);
            } catch (JwtException e) {
                throw new UnauthorizedException();
            }
        });
    }


    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(this.key)
                .parseClaimsJws(token)
                .getBody();
    }

}
