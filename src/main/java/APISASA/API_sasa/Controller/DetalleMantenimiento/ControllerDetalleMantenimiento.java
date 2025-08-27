package APISASA.API_sasa.Controller.DetalleMantenimiento;

import APISASA.API_sasa.Models.DTO.DetalleMantenimiento.DetalleMantenimientoDTO;
import APISASA.API_sasa.Services.DetalleMantenimiento.DetalleMantenimientoService;
import APISASA.API_sasa.Exceptions.ExceptionDetalleNoEncontrado;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detalles")
public class ControllerDetalleMantenimiento {

    @Autowired
    private DetalleMantenimientoService service;

    // 🔹 Consultar todos los detalles
    @GetMapping("/consultar")
    public List<DetalleMantenimientoDTO> obtenerDetalles() {
        return service.obtenerDetalles();
    }

    // 🔹 Consultar detalles de un mantenimiento específico
    @GetMapping("/mantenimiento/{idMantenimiento}")
    public ResponseEntity<?> obtenerPorMantenimiento(@PathVariable Long idMantenimiento) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerPorMantenimiento(idMantenimiento)
        ));
    }

    // 🔹 Registrar un nuevo detalle
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody DetalleMantenimientoDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        DetalleMantenimientoDTO creado = service.insertarDetalle(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", creado
        ));
    }

    // 🔹 Actualizar estado de un detalle
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DetalleMantenimientoDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
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
}
