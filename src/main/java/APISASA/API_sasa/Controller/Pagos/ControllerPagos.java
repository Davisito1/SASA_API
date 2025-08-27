package APISASA.API_sasa.Controller.Pagos;

import APISASA.API_sasa.Models.DTO.Pagos.PagosDTO;
import APISASA.API_sasa.Services.Pagos.PagosServices;
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
@RequestMapping("/apiPagos")
@CrossOrigin
public class ControllerPagos {

    @Autowired
    private PagosServices service;

    // 🔹 Consultar todos
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerPagos() {
        List<PagosDTO> lista = service.obtenerPagos();
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", lista
        ));
    }

    // 🔹 Registrar
    @PostMapping("/registrar")
    public ResponseEntity<?> insertarPago(@Valid @RequestBody PagosDTO dto, BindingResult result) {
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
                    "data", service.insertarPago(dto)
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarPago(
            @PathVariable Long id,
            @Valid @RequestBody PagosDTO dto,
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
            PagosDTO actualizado = service.actualizarPago(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Eliminar
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarPago(@PathVariable Long id) {
        try {
            if (service.eliminarPago(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Pago eliminado correctamente"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Pago no encontrado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar pago",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
