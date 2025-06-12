package com.github.ideepakkalra.eventmanagement.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Service
public class JWTService {
    // Base64 encoded (TestKeyTestKeyTestKeyTestKeyTestKey)
    private static final String SECRET_KEY = "VGVzdEtleVRlc3RLZXlUZXN0S2V5VGVzdEtleVRlc3RLZXk=";
    private static final Long EXPIRATION_ADMIN = 1800000L;
    private static final Long EXPIRATION_STANDARD = 31536000000L;
    public static final String CLAIM_ROLE = "role";
    public static final String CLAIM_SUB = "sub";

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateToken(String sub, String role) {
        return Jwts.builder()
                .setSubject(sub)
                .claim(CLAIM_ROLE, role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ("STANDARD".equals(role) ? EXPIRATION_STANDARD : EXPIRATION_ADMIN)))
                .signWith(getSecretKey())
                .compact(); // Creates the token
    }

    private Object getClaim(String token, String claimName) {
        try {
            Claims claims  = Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token.replace("Bearer ", "")).getBody();
            return claims.get(claimName);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getSub(String token) {
        return Long.parseLong((String) Objects.requireNonNull(getClaim(token, JWTService.CLAIM_SUB)));
    }

    public String getRole(String token) {
        return (String) Objects.requireNonNull(getClaim(token, JWTService.CLAIM_ROLE));
    }
}
