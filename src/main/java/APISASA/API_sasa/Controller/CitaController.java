package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Services.CitaService;
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
@RequestMapping("/apiCitas")
public class CitaController {

    @Autowired
    private CitaService service;

    // LISTAR TODAS
    @GetMapping("/consultar")
    public List<CitaDTO> obtenerCitas() {
        return service.obtenerCitas();
    }

    // ✅ OBTENER POR ID
    @GetMapping("/consultar/{id}")
    public ResponseEntity<?> obtenerCitaPorId(@PathVariable Long id) {
        try {
            CitaDTO dto = service.obtenerCitaPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", dto
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al consultar la cita",
                    "timestamp", Instant.now().toString()
            ));
        }
    }

    // CREAR
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCita(@Valid @RequestBody CitaDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", service.insertarCita(dto)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ACTUALIZAR
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @Valid @RequestBody CitaDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(service.actualizarCita(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ELIMINAR
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarCita(@PathVariable Long id) {
        try {
            if (service.eliminarCita(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Cita eliminada"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Cita no encontrada"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
