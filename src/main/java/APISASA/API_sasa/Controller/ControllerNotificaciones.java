package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.NotificacionDTO;
import APISASA.API_sasa.Services.NotificacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class ControllerNotificaciones {

    @Autowired
    private NotificacionesService service;

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
