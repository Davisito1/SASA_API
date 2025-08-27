package APISASA.API_sasa.Controller.Mantenimiento;

import APISASA.API_sasa.Models.DTO.Mantenimiento.MantenimientoDTO;
import APISASA.API_sasa.Services.Mantenimiento.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/apiMantenimiento")
@CrossOrigin(origins = "*")
public class ControllerMantenimiento {

    @Autowired
    private MantenimientoService service;

    // ========= LISTAR (SIN PAGINAR) =========
    // GET /apiMantenimiento/listar
    @GetMapping("/listar")
    public ResponseEntity<?> listarTodos() {
        List<MantenimientoDTO> lista = service.obtenerMantenimientos();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", lista
        ));
    }


    // GET /apiMantenimiento/consultar?page=0&size=10&sort=id,asc&q=texto
    @GetMapping("/consultar")
    public ResponseEntity<?> consultarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // Ajusta el campo por defecto a tu PK real si no es "id"
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String q
    ) {
        if (page < 0) page = 0;
        if (size < 1 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        // Parseo de sort seguro
        String[] parts = sort.split(",");
        String sortField = (parts.length > 0 && !parts[0].trim().isEmpty())
                ? parts[0].trim()
                : "id";
        Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<MantenimientoDTO> result = service.obtenerMantenimientosPaginado(q, pageable);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", result
        ));
    }

    // ========= REGISTRAR =========
    // POST /apiMantenimiento/registrar
    @PostMapping("/registrar")
    public ResponseEntity<?> insertarMantenimiento(
            @Valid @RequestBody MantenimientoDTO dto,
            BindingResult result
    ) {
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
                    "data", service.insertarMantenimiento(dto)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ========= ACTUALIZAR =========
    // PUT /apiMantenimiento/actualizar/{id}
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarMantenimiento(
            @PathVariable Long id,
            @Valid @RequestBody MantenimientoDTO dto,
            BindingResult result
    ) {
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
                    "data", service.actualizarMantenimiento(id, dto)
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

    // ========= ELIMINAR =========
    // DELETE /apiMantenimiento/eliminar/{id}
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
