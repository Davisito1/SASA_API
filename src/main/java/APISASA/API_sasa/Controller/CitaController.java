package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Services.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/apiCitas")
public class CitaController {

    @Autowired
    private CitaService service;

    // ✅ LISTAR PAGINADO + ORDENAMIENTO + BÚSQUEDA OPCIONAL
    // Ejemplos:
    // /apiCitas/consultar?page=0&size=10
    // /apiCitas/consultar?page=0&size=10&sort=fecha,desc
    // /apiCitas/consultar?q=Juan&page=1&size=5&sort=fecha,asc&sort=hora,asc
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerCitasPaginado(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) List<String> sort
    ) {
        if (size < 1 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de página debe estar entre 1 y 50"
            ));
        }
        if (page < 0) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El número de página debe ser >= 0"
            ));
        }

        Sort sortSpec = parseSort(sort); // UNSORTED si no envías nada
        Pageable pageable = PageRequest.of(page, size, sortSpec);

        try {
            Page<CitaDTO> result = (q != null && !q.isBlank())
                    ? service.buscarCitas(q.trim(), pageable)
                    : service.obtenerCitas(pageable);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status", "error",
                    "message", "Error al consultar las citas",
                    "detail", e.getMessage(),
                    "timestamp", Instant.now().toString()
            ));
        }
    }

    // ✅ OBTENER POR ID (igual que tenías)
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

    // ✅ CREAR (sin cambios funcionales)
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

    // ✅ ACTUALIZAR (igual)
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

    // ✅ ELIMINAR (igual)
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

    // ---- util ----
    private Sort parseSort(List<String> sortParams) {
        if (sortParams == null || sortParams.isEmpty()) return Sort.unsorted();
        List<Sort.Order> orders = new ArrayList<>();
        for (String param : sortParams) {
            if (param == null || param.isBlank()) continue;
            String[] parts = param.split(",");
            String property = parts[0].trim();
            Sort.Direction direction = Sort.Direction.ASC;
            if (parts.length > 1) {
                try {
                    direction = Sort.Direction.fromString(parts[1].trim());
                } catch (IllegalArgumentException ignored) {}
            }
            if (!property.isEmpty()) orders.add(new Sort.Order(direction, property));
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }
}
