package APISASA.API_sasa.Controller.Auth;

import APISASA.API_sasa.Models.DTO.Usuario.UserDTO;
import APISASA.API_sasa.Entities.Usuario.UserEntity;   // ← tu entidad real
import APISASA.API_sasa.Services.Auth.AuthService;
import APISASA.API_sasa.Utils.JWTUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;
    private final JWTUtils jwtUtils;

    public AuthController(AuthService service, JWTUtils jwtUtils) {
        this.service = service;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserDTO data) {
        // Acepta correo o nombreUsuario como identificador
        String identificador = (data.getCorreo() != null && !data.getCorreo().isBlank())
                ? data.getCorreo()
                : data.getNombreUsuario();

        if (identificador == null || identificador.isBlank()
                || data.getContrasena() == null || data.getContrasena().isBlank()) {
            return ResponseEntity.badRequest().body("Error: credenciales incompletas");
        }

        // Autenticar
        Optional<UserEntity> userOpt = service.authenticate(identificador, data.getContrasena());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Credenciales incorrectas");
        }

        UserEntity user = userOpt.get();

        // Claims del token
        String userId   = String.valueOf(user.getIdUsuario()); // simplificado
        String subject  = (user.getCorreo() != null && !user.getCorreo().isBlank())
                ? user.getCorreo()
                : user.getNombreUsuario();
        String roleName = (user.getRol() != null && !user.getRol().isBlank())
                ? user.getRol()
                : "USER";

        String token = jwtUtils.create(userId, subject, roleName);

        // Cookie segura (si front y API están en dominios distintos, SameSite=None + Secure)
        ResponseCookie cookie = ResponseCookie.from("authToken", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")   // Usa "Lax" si comparten dominio
                .path("/")
                .maxAge(Duration.ofDays(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Inicio de sesión exitoso");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        ResponseCookie clear = ResponseCookie.from("authToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clear.toString())
                .body("Sesión cerrada");
    }
}
