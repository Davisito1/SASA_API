package APISASA.API_sasa.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtils {

    @Value("${security.jwt.secret}")
    private String jwtSecreto;                  // Clave (mínimo 32 caracteres recomendados)
    @Value("${security.jwt.issuer}")
    private String issuer;                      // Firma del token
    @Value("${security.jwt.expiration}")
    private long expiracionMs;                  // Tiempo de expiración

    private final Logger log = LoggerFactory.getLogger(JWTUtils.class);

    // ================== NUEVO HELPER ==================
    private SecretKey getSigningKey() {
        // Usa la cadena tal cual en UTF-8 (ya no decodifica Base64)
        return Keys.hmacShaKeyFor(jwtSecreto.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Metodo para crear JWT
     */
    public String create(String id, String correo, String rol) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expiracionMs);

        return Jwts.builder()
                .setId(id)                                              // ID único (JWT ID)
                .setIssuedAt(now)                                       // Fecha de emisión
                .setSubject(correo)                                     // Sujeto (usuario)
                .claim("id", id)
                .claim("rol", rol)
                .setIssuer(issuer)                                      // Emisor del token
                .setExpiration(expiracionMs >= 0 ? expiration : null)   // Expiración
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)    // Firma con HS256
                .compact();
    }

    public String extractRol(String token) {
        Claims claims = parseToken(token);
        return claims.get("rol", String.class);
    }

    public String getValue(String jwt) {
        Claims claims = parseClaims(jwt);
        return claims.getSubject();
    }

    public String getKey(String jwt) {
        Claims claims = parseClaims(jwt);
        return claims.getId();
    }

    public Claims parseToken(String jwt) throws ExpiredJwtException, MalformedJwtException {
        return parseClaims(jwt);
    }

    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Token inválido: {}", e.getMessage());
            return false;
        }
    }

    // ================== COMPLEMENTARIO ==================
    private Claims parseClaims(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())  // Ahora usa la misma key
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}
