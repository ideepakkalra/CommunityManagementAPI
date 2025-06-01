package com.github.ideepakkalra.eventmanagement.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Service
public class JWTService {
    // Base64 encoded (TestKeyTestKeyTestKeyTestKeyTestKey)
    private static final String SECRET_KEY = "VGVzdEtleVRlc3RLZXlUZXN0S2V5VGVzdEtleVRlc3RLZXk=";
    private static final Long EXPIRATION = 1800000L;
    public static final String CLAIM_ROLES = "roles";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateToken(String sub, List<String> roles) {
        return Jwts.builder()
                .setSubject(sub)
                .claim(CLAIM_ROLES, roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSecretKey())
                .compact(); // Creates the token
    }

    /*public void getClaim(String token, String claimName) {
        try {
            Jwt<?, ?> jwtTokens = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJwt(token);
            System.out.println("JWT is valid");
        } catch (Exception e) {
            System.out.println("Invalid JWT");
        }
    }*/
}
