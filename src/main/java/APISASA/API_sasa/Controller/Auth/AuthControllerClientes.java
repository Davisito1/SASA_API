package APISASA.API_sasa.Controller.Auth;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Services.Auth.AuthServiceClientes;
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
@RequestMapping("/auth/cliente")
@RequiredArgsConstructor
public class AuthControllerClientes {

    private final AuthServiceClientes clienteAuthService;
    private final JWTUtils jwtUtils;

    @Value("${security.jwt.cookieName:authToken}")
    private String jwtCookieName;

    @Value("${app.env.prod:false}")
    private boolean isProd;

    @Value("${app.cookie.domain:}")
    private String cookieDomain;

    @Value("${app.auth.ttlHours:8}")
    private int ttlHours;

    // ✅ Registro de cliente
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody ClienteEntity data) {
        ClienteEntity cliente = clienteAuthService.registrar(data);
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "Cliente registrado correctamente",
                "cliente", Map.of(
                        "id", cliente.getId(),
                        "nombre", cliente.getNombre(),
                        "apellido", cliente.getApellido(),
                        "correo", cliente.getCorreo()
                )
        ));
    }

    // ✅ Login de cliente
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {
        String correo = data.get("correo");
        String contrasena = data.get("contrasena");

        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status","ERROR","message","Correo y contraseña son obligatorios"));
        }

        Optional<ClienteEntity> clienteOpt = clienteAuthService.authenticate(correo, contrasena);
        if (clienteOpt.isEmpty()) {
            return ResponseEntity.status(401).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("status","ERROR","message","Credenciales incorrectas"));
        }

        ClienteEntity cliente = clienteOpt.get();

        String userId   = String.valueOf(cliente.getId());
        String subject  = cliente.getCorreo();
        String roleName = "CLIENTE";
        String token    = jwtUtils.create(userId, subject, roleName);

        ResponseCookie cookie = buildJwtCookie(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "status","OK",
                        "token", token,
                        "cliente", Map.of(
                                "id", cliente.getId(),
                                "nombre", cliente.getNombre(),
                                "apellido", cliente.getApellido(),
                                "correo", cliente.getCorreo(),
                                "rol", roleName
                        )
                ));
    }

    // ✅ Logout
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logout() {
        ResponseCookie clear = clearJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clear.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("status","OK","message","Sesión cerrada"));
    }

    // ===== Helpers =====
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
