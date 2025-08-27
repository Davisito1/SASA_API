package APISASA.API_sasa.Controller.Vehiculo;

import APISASA.API_sasa.Models.DTO.Vehiculo.VehicleDTO;
import APISASA.API_sasa.Services.Vehiculo.VehicleService;
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
import java.util.Map;

@RestController
@RequestMapping("/apiVehiculo")
@CrossOrigin(origins = "*")
public class ControllerVehiculo {

    @Autowired
    private VehicleService service;

    // ✅ CONSULTAR DATOS PAGINADOS Y ORDENADOS
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerVehiculos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idVehiculo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        if (size < 1 || size > 100) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de página debe estar entre 1 y 100"
            ));
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("desc")
                        ? Sort.by(sortBy).descending()
                        : Sort.by(sortBy).ascending()
        );

        Page<VehicleDTO> result = service.obtenerVehiculosPaginado(pageable);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", result
        ));
    }

    // ✅ REGISTRAR
    @PostMapping("/registrar")
    public ResponseEntity<?> nuevoVehiculo(@Valid @RequestBody VehicleDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", service.insertarVehiculo(dto)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ ACTUALIZAR
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody VehicleDTO dto,
                                        BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", service.actualizarVehiculo(id, dto)
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ✅ ELIMINAR
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarVehiculo(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Vehículo eliminado"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Vehículo no encontrado"
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
