package APISASA.API_sasa.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);

    @Value("${security.jwt.cookieName:JWT_TOKEN}") // ✅ siempre busca la cookie correcta
    private String authCookieName;

    private final JWTUtils jwtUtils;

    // Métodos auxiliares
    private boolean isPreflight(HttpServletRequest req) {
        return "OPTIONS".equalsIgnoreCase(req.getMethod());
    }

    private boolean isPublic(HttpServletRequest req) {
        final String path = req.getRequestURI();
        final String method = req.getMethod();
        if (isPreflight(req)) return true;
        if ("POST".equals(method) && path.contains("/auth")) return true;
        if (path.startsWith("/api/public/")) return true;
        if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) return true;
        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return isPublic(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            if (token != null && !token.isBlank() &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                if (jwtUtils.validateToken(token)) {
                    Claims claims = jwtUtils.parseToken(token);
                    String username = claims.getSubject();
                    String rol = jwtUtils.extractRol(token);
                    Long id = jwtUtils.extractId(token);

                    // Normaliza el rol
                    if (rol == null || rol.isBlank()) rol = "USER";
                    rol = rol.toUpperCase();
                    if (!rol.startsWith("ROLE_")) rol = "ROLE_" + rol;

                    log.info("✅ Token válido → Usuario={} | Rol={} | Id={}", username, rol, id);

                    var authorities = Collections.singletonList(new SimpleGrantedAuthority(rol));

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    log.warn("⚠️ Token inválido o expirado");
                }
            }

        } catch (ExpiredJwtException e) {
            log.warn("⚠️ JWT expirado: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (MalformedJwtException e) {
            log.warn("⚠️ JWT malformado: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.error("❌ Error procesando JWT", e);
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1️⃣ Buscar en header Authorization
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }

        // 2️⃣ Buscar en cookie JWT_TOKEN (o nombre definido)
        Cookie cookie = WebUtils.getCookie(request, authCookieName);
        if (cookie != null) {
            return cookie.getValue();
        }

        // 3️⃣ Buscar cookie alternativa (compatibilidad)
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equalsIgnoreCase("JWT_TOKEN") ||
                            c.getName().equalsIgnoreCase("authToken"))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        return null;
    }
}
