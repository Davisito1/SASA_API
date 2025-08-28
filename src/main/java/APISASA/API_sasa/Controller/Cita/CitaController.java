package APISASA.API_sasa.Controller.Cita;

import APISASA.API_sasa.Models.DTO.Cita.CitaDTO;
import APISASA.API_sasa.Services.Cita.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiCitas")
@CrossOrigin
public class CitaController {

    @Autowired
    private CitaService service;

    @GetMapping("/listar")
    public ResponseEntity<?> listarTodas() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerCitas()
        ));
    }

    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (page < 0) page = 0;
        if (size < 1 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }
        Page<CitaDTO> pageResult = service.obtenerCitas(PageRequest.of(page, size));
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", pageResult
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
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
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> crear(@Valid @RequestBody CitaDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
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

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody CitaDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }
        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", service.actualizarCita(id, dto)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage(),
                    "timestamp", Instant.now().toString()
            ));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarCita(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Cita eliminada con éxito"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "No se encontró la cita para eliminar"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar cita",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
