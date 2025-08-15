package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.DetalleMantenimientoDTO;
import APISASA.API_sasa.Services.DetalleMantenimientoService;
import APISASA.API_sasa.Exceptions.ExceptionDetalleNoEncontrado;
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
@RequestMapping("/apiDetalles")
public class ControllerDetalleMantenimiento {

    @Autowired
    private DetalleMantenimientoService service;

    @GetMapping("/consultar")
    public List<DetalleMantenimientoDTO> obtenerDetalles() {
        return service.obtenerDetalles();
    }

    // 🔹 NUEVO: /page con paginación + búsqueda (estado o IDs)
    // Ejemplos:
    // GET /apiDetalles/page?page=0&size=10
    // GET /apiDetalles/page?size=5&sort=id,desc
    // GET /apiDetalles/page?q=pendiente
    // GET /apiDetalles/page?q=12   (buscará por idMantenimiento=12 OR idServicio=12 OR idTipoMantenimiento=12)
    @GetMapping("/page")
    public ResponseEntity<?> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String q
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;

        String[] sortParts = sort.split(",");
        String sortField = sortParts[0].trim().isEmpty() ? "id" : sortParts[0].trim();
        Sort.Direction direction = (sortParts.length > 1 && "desc".equalsIgnoreCase(sortParts[1].trim()))
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<DetalleMantenimientoDTO> result = service.obtenerDetallesPaginado(q, pageable);

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
    public ResponseEntity<?> registrar(
            @Valid @RequestBody DetalleMantenimientoDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errores.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            DetalleMantenimientoDTO creado = service.insertarDetalle(dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", creado
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DetalleMantenimientoDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errores.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            DetalleMantenimientoDTO actualizado = service.actualizarDetalle(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionDetalleNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarDetalle(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Detalle eliminado correctamente"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Detalle no encontrado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar detalle",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
