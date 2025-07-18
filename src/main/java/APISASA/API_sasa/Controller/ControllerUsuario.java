package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.UserDTO;
import APISASA.API_sasa.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiUsuario")
public class ControllerUsuario {

    @Autowired
    private UserService service;

    // CONSULTAR TODOS
    @GetMapping("/usuarios")
    public List<UserDTO> datosUsuarios() {
        return service.getAllUsers();
    }

    // REGISTRAR USUARIO
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

    // ACTUALIZAR USUARIO
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
            return ResponseEntity.ok(actualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    // ELIMINAR USUARIO
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
}
