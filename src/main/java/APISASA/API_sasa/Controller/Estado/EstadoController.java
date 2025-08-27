package APISASA.API_sasa.Controller.Estado;

import APISASA.API_sasa.Models.DTO.Estado.EstadoDTO;
import APISASA.API_sasa.Services.Estado.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/estadoVehiculo")
public class EstadoController {

    @Autowired
    private EstadoService service;

    // 🔹 Consultar con paginación
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerEstados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (size <= 0 || size > 100) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "El tamaño de página debe estar entre 1 y 100"
            ));
        }

        Page<EstadoDTO> estados = service.getAllEstados(page, size);

        if (estados == null || estados.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "No hay estados registrados"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", estados
        ));
    }

    // 🔹 Consultar todos sin paginación (para catálogos)
    @GetMapping("/listar")
    public ResponseEntity<?> listarTodos() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.getAllEstadosSinPaginacion()
        ));
    }
}
