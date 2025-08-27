package APISASA.API_sasa.Controller.Usuario;

import APISASA.API_sasa.Models.DTO.Usuario.UserDTO;
import APISASA.API_sasa.Services.Usuario.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiUsuario")
@CrossOrigin(origins = "*") // ✅ Permite CORS
public class ControllerUsuario {

    @Autowired
    private UserService service;

    // CONSULTAR TODOS - CORREGIDO
    @GetMapping("/consultar")
    public ResponseEntity<?> datosUsuario(
                                           @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size
    ) {
        // Validar tamaño de página
        if (size <= 0 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of( //return directo
                    "status", "error",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        try {
            Page<UserDTO> usuarios = service.getAllUsers(page, size);

            if (usuarios == null || usuarios.isEmpty()) {
                return ResponseEntity.ok(Map.of( // JSON EN VES DE NULL
                        "status", "success",
                        "data", Map.of(
                                "content", java.util.List.of(),
                                "totalPages", 0,
                                "totalElements", 0,
                                "empty", true
                        ),
                        "message", "No hay usuarios registrados"
                ));
            }

            return ResponseEntity.ok(Map.of( // ✅ Envuelve en estructura consistente
                    "status", "success",
                    "data", usuarios,
                    "message", "Usuarios obtenidos correctamente"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al obtener usuarios: " + e.getMessage()
            ));
        }
    }

    // REGISTRAR USUARIO - OK
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@Valid @RequestBody UserDTO json, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            UserDTO creado = service.insertUser(json);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "data", creado
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al registrar usuario",
                    "detail", e.getMessage()
            ));
        }
    }

    // ACTUALIZAR USUARIO - OK
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @Valid @RequestBody UserDTO json, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            UserDTO actualizado = service.updateUser(id, json);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // ELIMINAR USUARIO - OK
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            if (!service.deleteUser(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                        "status", "error",
                        "message", "Usuario no encontrado",
                        "timestamp", Instant.now().toString()
                ));
            }

            return ResponseEntity.ok().body(Map.of(
                    "status", "success",
                    "message", "Usuario eliminado correctamente"
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar usuario",
                    "detail", e.getMessage()
            ));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        try {
            String nombreUsuario = credentials.get("nombreUsuario");
            String contrasena = credentials.get("contrasena");

            // Aquí debes implementar la lógica de autenticación en tu Service
            // Por ahora devolveré un ejemplo
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", Map.of(
                            "id", 1,
                            "nombreUsuario", nombreUsuario,
                            "rol", "Administrador"
                    ),
                    "message", "Login exitoso"
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "status", "error",
                    "message", "Credenciales incorrectas"
            ));
        }
    }
}