// src/main/java/APISASA/API_sasa/Utils/JWTUtils.java
package APISASA.API_sasa.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${security.jwt.secret:MiClaveUltraSecretaParaJWT1234567890}")
    private String jwtSecret;

    @Value("${security.jwt.expiration:86400000}") // 1 día
    private long jwtExpirationMs;

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ✅ Versión básica (web): username + rol
    public String generateToken(String username, String rol) {
        return generateToken(username, rol, null);
    }

    // ✅ Versión completa (web y móvil): username + rol + idCliente
    public String generateToken(String username, String rol, Long idCliente) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .claim("rol", rol.toUpperCase())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256);

        if (idCliente != null) {
            builder.claim("id", idCliente);
        }

        return builder.compact();
    }

    // ===============================
    // Validación y parsing
    // ===============================
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    public String extractRol(String token) {
        Object rol = parseToken(token).get("rol");
        return rol != null ? rol.toString() : null;
    }

    public Long extractId(String token) {
        Object id = parseToken(token).get("id");
        return id != null ? Long.parseLong(id.toString()) : null;
    }
}
