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

    // Clave secreta para firmar los JWT
    @Value("${security.jwt.secret:MiClaveUltraSecretaParaJWT1234567890}")
    private String jwtSecret;

    // Tiempo de expiración del token en milisegundos
    // Por defecto 86400000 ms = 1 día, pero en application.properties
    // se puede sobrescribir con 900000 (15 minutos)
    @Value("${security.jwt.expiration:86400000}")
    private long jwtExpirationMs;

    // Obtiene la clave de firma a partir del secreto configurado
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Genera un token con username y rol (básico)
    public String generateToken(String username, String rol) {
        return generateToken(username, rol, null);
    }

    // Genera un token completo con username, rol e idCliente (si aplica)
    public String generateToken(String username, String rol, Long idCliente) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username) // "sub" = nombre de usuario
                .claim("rol", rol.toUpperCase()) // claim con el rol
                .setIssuedAt(new Date()) // fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs)) // expiración = ahora + duración configurada
                .signWith(getSignKey(), SignatureAlgorithm.HS256); // firmado con HMAC-SHA256

        if (idCliente != null) {
            builder.claim("id", idCliente); // claim extra con id del usuario/cliente
        }

        return builder.compact();
    }

    // ===============================
    // Validación y lectura de tokens
    // ===============================

    // Verifica si el token es válido (firma correcta y no expirado)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Devuelve todos los claims del token
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extrae el username ("sub")
    public String extractUsername(String token) {
        return parseToken(token).getSubject();
    }

    // Extrae el rol (claim "rol")
    public String extractRol(String token) {
        Object rol = parseToken(token).get("rol");
        return rol != null ? rol.toString() : null;
    }

    // Extrae el id (claim "id")
    public Long extractId(String token) {
        Object id = parseToken(token).get("id");
        return id != null ? Long.parseLong(id.toString()) : null;
    }
}
