// src/main/java/APISASA/API_sasa/Controller/Auth/AuthController.java
package APISASA.API_sasa.Controller.Auth;

import APISASA.API_sasa.Models.DTO.Usuario.UserDTO;
import APISASA.API_sasa.Entities.Usuario.UserEntity;
import APISASA.API_sasa.Services.Auth.AuthService;
import APISASA.API_sasa.Utils.JWTUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth") // ⬅️ COINCIDE con tu LoginService.js
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JWTUtils jwtUtils;

    @Value("${security.jwt.cookieName:authToken}") // ⬅️ debe coincidir con el filtro
    private String jwtCookieName;

    @Value("${app.env.prod:false}")
    private boolean isProd;

    @Value("${app.cookie.domain:}")
    private String cookieDomain;

    @Value("${app.auth.ttlHours:8}")
    private int ttlHours;

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO data) {
        final String identificador = (data.getCorreo() != null && !data.getCorreo().isBlank())
                ? data.getCorreo() : data.getNombreUsuario();

        if (identificador == null || identificador.isBlank()
                || data.getContrasena() == null || data.getContrasena().isBlank()) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status","ERROR","message","Credenciales incompletas"));
        }

        Optional<UserEntity> userOpt = authService.authenticate(identificador, data.getContrasena());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status","ERROR","message","Credenciales incorrectas"));
        }

        UserEntity user = userOpt.get();

        // Construir JWT
        String userId   = String.valueOf(user.getIdUsuario());
        String subject  = (user.getCorreo() != null && !user.getCorreo().isBlank())
                ? user.getCorreo() : user.getNombreUsuario();
        String roleName = (user.getRol() != null && !user.getRol().isBlank()) ? user.getRol() : "USER";
        String token    = jwtUtils.generateToken(subject, roleName);

        // Cookie (opcional), el front usará Bearer en dev
        ResponseCookie cookie = buildJwtCookie(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "status","OK",
                        "token", token, // ⬅️ Tu LoginService lo guarda para Authorization: Bearer
                        "user", Map.of(
                                "id", user.getIdUsuario(),
                                "username", user.getNombreUsuario(),
                                "email", user.getCorreo(),
                                "rol", user.getRol()
                        )
                ));
    }

    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout() {
        ResponseCookie clear = clearJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clear.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("status","OK","message","Sesión cerrada"));
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> me(org.springframework.security.core.Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body(Map.of("status","ERROR","message","No autenticado"));
        }
        return ResponseEntity.ok(Map.of(
                "status","OK",
                "user", Map.of(
                        "username", auth.getName(),
                        "roles", auth.getAuthorities().stream().map(a -> a.getAuthority()).toList()
                )
        ));
    }

    // ===== Helpers cookie =====
    private ResponseCookie buildJwtCookie(String token) {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(jwtCookieName, token)
                .httpOnly(true).path("/").maxAge(Duration.ofHours(ttlHours));
        if (cookieDomain != null && !cookieDomain.isBlank()) b.domain(cookieDomain);
        if (isProd) b.sameSite("None").secure(true); else b.sameSite("Lax").secure(false);
        return b.build();
    }

    private ResponseCookie clearJwtCookie() {
        ResponseCookie.ResponseCookieBuilder b = ResponseCookie.from(jwtCookieName, "")
                .httpOnly(true).path("/").maxAge(0);
        if (cookieDomain != null && !cookieDomain.isBlank()) b.domain(cookieDomain);
        if (isProd) b.sameSite("None").secure(true); else b.sameSite("Lax").secure(false);
        return b.build();
    }
}
