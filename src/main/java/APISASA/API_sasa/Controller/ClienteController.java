package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.ClientDTO;
import APISASA.API_sasa.Models.DTO.ClientDTO;
import APISASA.API_sasa.Services.ClienteService;
import APISASA.API_sasa.Exceptions.ExceptionClienteNoEncontrado;
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
@RequestMapping("/apiCliente")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping("/consultar")
    public List<ClientDTO> obtenerClientes() {
        return service.obtenerClientes();
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody ClientDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
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

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody ClientDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errores = new HashMap<>();
            result.getFieldErrors().forEach(err -> errores.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errores);
        }

        try {
            ClientDTO actualizado = service.actualizarCliente(id, dto);
            return ResponseEntity.ok(actualizado);
        } catch (ExceptionClienteNoEncontrado e) {
            return ResponseEntity.status(404).body(Map.of(
                    "status", "error",
                    "message", e.getMessage()
            ));
        }
    }

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
