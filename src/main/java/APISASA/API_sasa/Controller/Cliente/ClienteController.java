package APISASA.API_sasa.Controller.Cliente;

import APISASA.API_sasa.Models.DTO.Cliente.ClientDTO;
import APISASA.API_sasa.Services.Cliente.ClienteService;
import APISASA.API_sasa.Exceptions.ExceptionClienteNoEncontrado;
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
@RequestMapping("/apiCliente")
@CrossOrigin
public class ClienteController {

    @Autowired
    private ClienteService service;

    // Consultar con paginación
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerClientes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        if (size <= 0 || size > 100) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de la página debe estar entre 1 y 100"
            ));
        }

        Page<ClientDTO> clientes = service.obtenerClientes(page, size);

        if (clientes.getContent().isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "No hay clientes registrados",
                    "data", clientes
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", clientes
        ));
    }

    //  Consultar cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        try {
            ClientDTO cliente = service.obtenerClientePorId(id);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", cliente
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    //  Registrar cliente
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(
            @Valid @RequestBody ClientDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors()
                    .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        try {
            ClientDTO creado = service.insertarCliente(dto);
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

    //  Actualizar cliente
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClientDTO dto,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors()
                    .forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "errors", errores
            ));
        }

        try {
            ClientDTO actualizado = service.actualizarCliente(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

    @PatchMapping("/actualizar-parcial/{id}")
    public ResponseEntity<?> actualizarParcial(
            @PathVariable Long id,
            @RequestBody ClientDTO dto
    ) {
        try {
            ClientDTO actualizado = service.actualizarClienteParcial(id, dto);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", actualizado
            ));
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al actualizar cliente",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
    // Eliminar cliente
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            if (service.eliminarCliente(id)) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Cliente eliminado correctamente"
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                        "status", "error",
                        "message", "Cliente no encontrado"
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "status", "error",
                    "message", "Error al eliminar cliente",
                    "timestamp", Instant.now().toString()
            ));
        }
    }
}
