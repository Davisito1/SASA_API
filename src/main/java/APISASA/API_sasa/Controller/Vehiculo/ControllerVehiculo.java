package APISASA.API_sasa.Controller.Vehiculo;

import APISASA.API_sasa.Models.DTO.Vehiculo.VehicleDTO;
import APISASA.API_sasa.Services.Vehiculo.VehicleService;
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
@RequestMapping("/apiVehiculo")
@CrossOrigin(origins = "*")
public class ControllerVehiculo {

    @Autowired
    private VehicleService service;

    // CONSULTAR DATOS
    @GetMapping ("consultar")
    public List<VehicleDTO> obtenerVehiculos() {
        return service.obtenerVehiculos();
    }

    // REGISTRAR
    @PostMapping ("registrar")
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

    // ACTUALIZAR
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
            return ResponseEntity.ok(service.actualizarVehiculo(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // ELIMINAR
    @DeleteMapping("eliminar/{id}")
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
