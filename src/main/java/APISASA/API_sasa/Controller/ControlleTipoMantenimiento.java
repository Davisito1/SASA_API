package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.MetodoPagoDTO;
import APISASA.API_sasa.Models.DTO.TipoMantenimientoDTO;
import APISASA.API_sasa.Services.TipoMantenimientoService;
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
@RequestMapping("/apiTipoMantenimiento")
public class ControlleTipoMantenimiento {
    @Autowired
    private TipoMantenimientoService service;

    @GetMapping("/consultar")
    public List<TipoMantenimientoDTO> obtenerTipos() {
        return service.obtenerTipos();
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> insertarTipo(@Valid @RequestBody TipoMantenimientoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", service.insertarTipo(dto)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarMetodo(@PathVariable Long id, @Valid @RequestBody TipoMantenimientoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(service.actualizarTipo(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarMetodo(@PathVariable Long id) {
        try {
            if (service.eliminarTipo(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Tipo de mantenimiento eliminado"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Tipo de mantenimiento no encontrado"
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
