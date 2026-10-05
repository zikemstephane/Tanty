package com.NTFOODS.Api_tanty.modules.auth.infrastructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expirationMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /** Génère un token embarquant le matricule, le rôle et l'identifiant de session (sid). */
    public String genererToken(String matricule, String role, String sessionId) {
        return Jwts.builder()
            .subject(matricule)
            .claim("role", role)
            .claim("sid", sessionId)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(secretKey)
            .compact();
    }

    /** @deprecated conservé pour compatibilité ; préférer la variante avec sessionId (session unique). */
    @Deprecated
    public String genererToken(String matricule, String role) {
        return genererToken(matricule, role, null);
    }

    public String extraireMatricule(String token) {
        return extraireClaims(token).getSubject();
    }

    public String extraireRole(String token) {
        return (String) extraireClaims(token).get("role");
    }

    public String extraireSessionId(String token) {
        return (String) extraireClaims(token).get("sid");
    }

    public boolean estValide(String token) {
        try {
            extraireClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public long getExpirationMs() { return expirationMs; }

    private Claims extraireClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
