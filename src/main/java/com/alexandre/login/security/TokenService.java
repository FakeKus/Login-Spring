package com.alexandre.login.security;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alexandre.login.form.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String key;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);

            String token = JWT.create()
                    .withIssuer("Login API")
                    .withSubject(user.getEmail())
                    .withExpiresAt(this.getExpirationTime())
                    .sign(algorithm);
            
            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);

            return JWT.require(algorithm)
                    .withIssuer("Login API")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }

    private Instant getExpirationTime() {
        return Instant.now().plusSeconds(7200);
    }    
}
