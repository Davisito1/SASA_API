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
public class ControllerFactura {

    @Autowired
    private FacturaService service;

    @GetMapping("/consultar")
    private ResponseEntity<Page<FacturaDTO>> obtenerHistorial(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        if (size <= 0 || size > 50){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "El tamaño de la página debe estar entre 1 y 50"
            ));
            return ResponseEntity.ok(null);
        }
        Page<FacturaDTO> categories = service.obtenerFacturas(page, size);
        if (categories == null){
            ResponseEntity.badRequest().body(Map.of(
                    "status", "No hay facturas registradas"
            ));
        }
        return ResponseEntity.ok(categories);
    }

    // 🔹 NUEVO ENDPOINT: Obtener factura por ID
    @GetMapping("/{id}")
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
            return ResponseEntity.badRequest().body(errores);
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
            return ResponseEntity.badRequest().body(errores);
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

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarFactura(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Factura eliminada correctamente"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Factura no encontrada"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar factura",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
