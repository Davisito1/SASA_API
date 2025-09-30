package APISASA.API_sasa.Controller.Notificaciones;

import APISASA.API_sasa.Models.DTO.Notificaciones.NotificacionDTO;
import APISASA.API_sasa.Services.Notificaciones.NotificacionesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class ControllerNotificaciones {

    @Autowired
    private NotificacionesService service;

    // 🔹 Crear nueva notificación
    @PostMapping("/crear")
    public ResponseEntity<?> crearNotificacion(@Valid @RequestBody NotificacionDTO dto) {
        try {
            NotificacionDTO creada = service.crearNotificacion(dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", creada
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Consultar todas las notificaciones de un usuario
    @GetMapping("/listar/{idUsuario}")
    public ResponseEntity<?> obtenerPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerPorUsuario(idUsuario)
        ));
    }

    // 🔹 Marcar como leída
    @PutMapping("/marcarLeida/{id}")
    public ResponseEntity<?> marcarLeida(@PathVariable Long id) {
        try {
            NotificacionDTO actualizada = service.marcarLeida(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizada
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Eliminar notificación
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (service.eliminarNotificacion(id)) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Notificación eliminada correctamente"
            ));
        } else {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "Notificación no encontrada"
            ));
        }
    }
}
