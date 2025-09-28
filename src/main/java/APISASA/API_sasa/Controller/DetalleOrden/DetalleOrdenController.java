package APISASA.API_sasa.Controller.DetalleOrden;

import APISASA.API_sasa.Models.DTO.DetalleOrden.DetalleOrdenDTO;
import APISASA.API_sasa.Exceptions.ExceptionDetalleOrdenNoEncontrado; // ✅ import correcto
import APISASA.API_sasa.Services.DetalleOrden.DetalleOrdenService;   // ✅ service correcto
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
@RequestMapping("/apiDetalleOrden")
@CrossOrigin
public class DetalleOrdenController {

    @Autowired
    private DetalleOrdenService service;

    // 🔹 Consultar todos los detalles
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerDetalles() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerDetalles()
        ));
    }

    // 🔹 Consultar por ID detalle
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            DetalleOrdenDTO dto = service.obtenerPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", dto
            ));
        } catch (ExceptionDetalleOrdenNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Consultar detalles por ID de orden
    @GetMapping("/porOrden/{idOrden}")
    public ResponseEntity<?> obtenerPorOrden(@PathVariable Long idOrden) {
        List<DetalleOrdenDTO> lista = service.obtenerPorOrden(idOrden);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", lista
        ));
    }

    // 🔹 Registrar detalle
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody DetalleOrdenDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errores.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        DetalleOrdenDTO creado = service.insertarDetalle(dto);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", creado
        ));
    }

    // 🔹 Actualizar detalle
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DetalleOrdenDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err ->
                    errores.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        try {
            DetalleOrdenDTO actualizado = service.actualizarDetalle(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionDetalleOrdenNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Eliminar detalle
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
