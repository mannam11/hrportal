package com.app.hrportal.service;

import com.app.hrportal.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final Algorithm algorithm;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateAccessToken(User user) {
        return JWT.create()
                .withSubject(user.getId())
                .withClaim("role", user.getRole().name())
                .withIssuedAt(new Date())
                .withExpiresAt(
                        new Date(System.currentTimeMillis() + 15 * 60 * 1000)
                )
                .sign(algorithm);
    }

    public DecodedJWT verify(String token) {
        return JWT.require(algorithm).build().verify(token);
    }
}

