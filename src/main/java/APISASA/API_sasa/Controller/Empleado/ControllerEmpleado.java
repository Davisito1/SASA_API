package APISASA.API_sasa.Controller.Empleado;

import APISASA.API_sasa.Exceptions.ExceptionEmpleadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.Empleado.EmpleadoDTO;
import APISASA.API_sasa.Services.Empleado.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/apiEmpleados")
@CrossOrigin
public class ControllerEmpleado {

    @Autowired
    private EmpleadoService service;

    // 🔹 Consultar con paginación y búsqueda opcional
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerEmpleados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String q
    ) {
        if (page < 0) page = 0;
        if (size < 1 || size > 50) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de la página debe estar entre 1 y 50"
            ));
        }

        Page<EmpleadoDTO> pageResult = service.obtenerEmpleadosPaginado(q, PageRequest.of(page, size));

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", pageResult
        ));
    }

    // 🔹 Registrar
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody EmpleadoDTO dto,
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
            EmpleadoDTO creado = service.insertarEmpleado(dto);
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

    // 🔹 Actualizar
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmpleadoDTO dto,
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
            EmpleadoDTO actualizado = service.actualizarEmpleado(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionEmpleadoNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    // 🔹 Eliminar
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarEmpleado(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Empleado eliminado correctamente"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Empleado no encontrado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar empleado",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
