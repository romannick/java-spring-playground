package com.nikolayr.playground.gatewayService.util.auth;

import com.nikolayr.playground.shared.exception.UnauthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Base64;

@Component
public class JwtUtils {
    private final Key key;

    public JwtUtils(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public Mono<Void> verifyAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Mono.error( new UnauthorizedException());
        }

        String token = authHeader.substring(7);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(this.key)
                    .build()
                    .parseClaimsJws(token);

            return Mono.empty();
        } catch (ExpiredJwtException e) {
            return Mono.error( new UnauthorizedException());
        } catch (JwtException e) {
            return Mono.error( new UnauthorizedException());
        }

        // Have 2 separate catch blocks so later we can maybe add different logging
    }
}
