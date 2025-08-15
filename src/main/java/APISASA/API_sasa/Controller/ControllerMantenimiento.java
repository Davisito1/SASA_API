package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.MantenimientoDTO;
import APISASA.API_sasa.Services.MantenimientoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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

    // 🔹 NUEVO: paginado + búsqueda opcional
    // Ejemplos:
    //  GET /apiMantenimiento/page?page=0&size=10
    //  GET /apiMantenimiento/page?sort=id,desc
    //  GET /apiMantenimiento/page?q=pendiente
    //  GET /apiMantenimiento/page?q=25  (busca por ID exacto; si no existe, intenta idVehiculo=25 si lo habilitas en repo)
    @GetMapping("/page")
    public ResponseEntity<?> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            // ⚠️ Si tu PK no se llama "id", cambia el default a tu campo real (p. ej., "idMantenimiento")
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String q
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        String[] parts = sort.split(",");
        String sortField = parts[0].trim().isEmpty() ? "id" : parts[0].trim();
        Sort.Direction direction = (parts.length > 1 && "desc".equalsIgnoreCase(parts[1].trim()))
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<MantenimientoDTO> result = service.obtenerMantenimientosPaginado(q, pageable);

        Map<String, Object> body = new HashMap<>();
        body.put("status", "success");
        body.put("page", result.getNumber());
        body.put("size", result.getSize());
        body.put("totalPages", result.getTotalPages());
        body.put("totalElements", result.getTotalElements());
        body.put("sort", sort);
        body.put("query", q);
        body.put("data", result.getContent());
        return ResponseEntity.ok(body);
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
