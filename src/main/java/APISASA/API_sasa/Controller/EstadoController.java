package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Exceptions.ExceptionEstadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.EstadoDTO;
import APISASA.API_sasa.Services.EstadoService;
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
@RequestMapping("/apiEstadoVehiculo")
public class EstadoController {

    @Autowired
    private EstadoService service;

    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerEstados() {
        List<EstadoDTO> estados = service.getAllEstados();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", estados
        ));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody EstadoDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors()
                    .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        try {
            EstadoDTO creado = service.createEstado(dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", creado
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage(),
                    "timestamp", Instant.now().toString()
            ));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EstadoDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors()
                    .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        try {
            EstadoDTO actualizado = service.updateEstado(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionEstadoNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error inesperado al actualizar estado",
                    "timestamp", Instant.now().toString()
            ));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.deleteEstado(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Estado eliminado correctamente"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Estado no encontrado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar estado",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
