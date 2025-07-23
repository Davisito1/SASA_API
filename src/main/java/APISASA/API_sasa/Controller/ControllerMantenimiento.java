package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Entities.CitaEntity;
import APISASA.API_sasa.Entities.MantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionCitaNoEncontrada;
import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Models.DTO.MantenimientoDTO;
import APISASA.API_sasa.Repositories.CitaRepository;
import APISASA.API_sasa.Repositories.MantenimientoRepository;
import APISASA.API_sasa.Services.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiMantenimiento")
public class ControllerMantenimiento {
    @Autowired
    private MantenimientoService service;

    @GetMapping("/consultar")
    public List<MantenimientoDTO> obtenerMantenimientos() {
        return service.obtenerMantenimientos();
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> insertarMantenimiento(@Valid @RequestBody MantenimientoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", service.insertarMantenimiento(dto)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarMantenimiento(@PathVariable Long id, @Valid @RequestBody MantenimientoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(service.actualizarMantenimiento(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarMantenimiento(@PathVariable Long id) {
        try {
            if (service.eliminarMantenimiento(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Mantenimiento eliminado"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Mantenimiento no encontrado"
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
