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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);

    @Value("${security.jwt.cookieName:JWT}")
    private String authCookieName;

    private final JWTUtils jwtUtils;

    // --- Helpers de path ---
    private boolean isPreflight(HttpServletRequest req) {
        return "OPTIONS".equalsIgnoreCase(req.getMethod());
    }

    private boolean isPublic(HttpServletRequest req) {
        final String path = req.getRequestURI();
        final String method = req.getMethod();
        // Ajusta estas rutas según tu app
        if (isPreflight(req)) return true;
        if ("POST".equals(method) && ("/auth/login".equals(path) || "/api/auth/login".equals(path))) return true;
        if ("POST".equals(method) && ("/auth/logout".equals(path) || "/api/auth/logout".equals(path))) return true;
        if ("POST".equals(method) && ("/auth/register".equals(path) || "/api/auth/register".equals(path))) return true;
        if (path.startsWith("/api/public/")) return true;
        // Swagger (si lo usas) – quítalo si no aplica
        if (path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs")) return true;
        return false;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // No filtrar preflight ni endpoints públicos
        return isPublic(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        try {
            String token = resolveToken(request);

            if (token != null && !token.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Valida y construye autenticación
                Claims claims = jwtUtils.parseToken(token); // lanza si inválido/expirado
                String username = claims.getSubject();
                String rol = jwtUtils.extractRol(token); // asegúrate de que devuelve algo tipo "Administrador" o "ROLE_ADMIN"

                // Normaliza el prefijo ROLE_
                String roleName = (rol != null && rol.startsWith("ROLE_")) ? rol : "ROLE_" + (rol == null ? "USER" : rol);

                Collection<? extends GrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority(roleName));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }

        } catch (ExpiredJwtException e) {
            log.warn("JWT expirado: {}", e.getMessage());
            // Limpia el contexto y deja que el entry point responda 401 si la ruta lo requiere
            SecurityContextHolder.clearContext();
        } catch (MalformedJwtException e) {
            log.warn("JWT malformado: {}", e.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.error("Error procesando JWT", e);
            SecurityContextHolder.clearContext();
        }

        // Continúa siempre: Security decidirá (permitAll / authenticated)
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 1) Header Authorization: Bearer
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7).trim();
        }
        // 2) Cookie HttpOnly
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            return Arrays.stream(cookies)
                    .filter(c -> authCookieName.equals(c.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }
        return null;
    }
}
