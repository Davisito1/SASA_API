package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Exceptions.ExceptionHistorialNoEncontrado;
import APISASA.API_sasa.Models.DTO.HistorialDTO;
import APISASA.API_sasa.Services.HistorialService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiHistorial")
public class ControllerHistorial {

    @Autowired
    private HistorialService service;

    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerTodos() {
        List<HistorialDTO> lista = service.obtenerHistoriales();
        return ResponseEntity.ok(Map.of("status", "success", "data", lista));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody HistorialDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("status", "error", "errors", errores));
        }

        try {
            HistorialDTO creado = service.insertarHistorial(dto);
            return ResponseEntity.ok(Map.of("status", "success", "data", creado));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage(),
                    "timestamp", Instant.now().toString()
            ));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody HistorialDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of("status", "error", "errors", errores));
        }

        try {
            HistorialDTO actualizado = service.actualizarHistorial(id, dto);
            return ResponseEntity.ok(Map.of("status", "success", "data", actualizado));
        } catch (ExceptionHistorialNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarHistorial(id)) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "Historial eliminado correctamente"));
            } else {
                return ResponseEntity.status(404).body(Map.of("status", "error", "message", "Historial no encontrado"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar historial",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
