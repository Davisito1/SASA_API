package APISASA.API_sasa.Controller.Factura;

import APISASA.API_sasa.Models.DTO.Factura.FacturaDTO;
import APISASA.API_sasa.Services.Factura.FacturaService;
import APISASA.API_sasa.Exceptions.ExceptionFacturaNoEncontrada;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiFactura")
@CrossOrigin
public class ControllerFactura {

    @Autowired
    private FacturaService service;

    // 🔹 Consultar facturas con paginación
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerFacturas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (size <= 0 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        Page<FacturaDTO> facturas = service.obtenerFacturas(page, size);

        if (facturas == null || facturas.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "No hay facturas registradas"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", facturas
        ));
    }

    // 🔹 Obtener factura por ID (solo números)
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            FacturaDTO dto = service.obtenerFacturaPorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", dto
            ));
        } catch (ExceptionFacturaNoEncontrada e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Registrar factura
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody FacturaDTO dto,
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
            FacturaDTO creado = service.insertarFactura(dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", creado
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Actualizar factura
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FacturaDTO dto,
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
            FacturaDTO actualizado = service.actualizarFactura(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionFacturaNoEncontrada e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Anular factura (estado = Cancelada)
    @PutMapping("/{id:\\d+}/anular")
    public ResponseEntity<?> anular(@PathVariable Long id) {
        try {
            FacturaDTO anulada = service.anularFactura(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", anulada,
                    "message", "Factura anulada correctamente"
            ));
        } catch (ExceptionFacturaNoEncontrada e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al anular factura",
                    "timestamp", Instant.now().toString()
            ));
        }
    }

}
