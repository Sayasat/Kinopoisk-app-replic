package com.sayasat.exp2.kinopoisk_check.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt_secret}")
    private String secret;
    private static final String SUBJECT = "User Details";
    private static final String ISSUER = "sayasat";


    public String generateToken(String username, String role) {
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

        return JWT.create()
                .withSubject(SUBJECT)
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withIssuer(ISSUER)
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    // Метод для получения роли из токена
    public String getRoleFromToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("role").asString();
    }

    public String getUsernameFromToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject(SUBJECT)
                .withIssuer(ISSUER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }
}
