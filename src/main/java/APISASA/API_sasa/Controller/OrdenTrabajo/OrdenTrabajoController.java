package APISASA.API_sasa.Controller.OrdenTrabajo;

import APISASA.API_sasa.Models.DTO.OrdenTrabajo.OrdenTrabajoDTO;
import APISASA.API_sasa.Services.OrdenTrabajo.OrdenTrabajoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiOrdenTrabajo")
@CrossOrigin
public class OrdenTrabajoController {

    @Autowired
    private OrdenTrabajoService service;

    // 🔹 Consultar todas las órdenes de trabajo
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerOrdenes() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerOrdenes()
        ));
    }

    // 🔹 Consultar por ID
    @GetMapping("/consultar/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        OrdenTrabajoDTO dto = service.obtenerPorId(id);
        if (dto == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "Orden de trabajo no encontrada"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", dto
        ));
    }

    // 🔹 Crear nueva orden de trabajo
    @PostMapping("/crear")
    public ResponseEntity<?> crear(@Valid @RequestBody OrdenTrabajoDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.crearOrden(dto)
        ));
    }

    // 🔹 Actualizar orden de trabajo
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @Valid @RequestBody OrdenTrabajoDTO dto,
                                        BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                    errores.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        OrdenTrabajoDTO actualizado = service.actualizarOrden(id, dto);
        if (actualizado == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "Orden de trabajo no encontrada"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", actualizado
        ));
    }

    // 🔹 Eliminar orden de trabajo
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        boolean eliminado = service.eliminarOrden(id);
        if (!eliminado) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", "Orden de trabajo no encontrada"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Orden de trabajo eliminada correctamente"
        ));
    }
}
