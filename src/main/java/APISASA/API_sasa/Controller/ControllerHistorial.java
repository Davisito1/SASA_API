package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.HistorialDTO;
import APISASA.API_sasa.Services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/historial")
public class ControllerHistorial {

    @Autowired
    private HistorialService service;

    // 🔹 Consultar con paginación
    @GetMapping("/consultar")
    public ResponseEntity<?> obtenerHistorial(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<HistorialDTO> historial = service.obtenerHistorial(page, size);

        if (historial == null || historial.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "data", "No hay registros de historial"
            ));
        }

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", historial
        ));
    }

    // 🔹 Consultar historial por vehículo
    @GetMapping("/vehiculo/{idVehiculo}")
    public ResponseEntity<?> obtenerHistorialPorVehiculo(@PathVariable Long idVehiculo) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerPorVehiculo(idVehiculo)
        ));
    }

    // 🔹 Registrar historial manual (opcional, uso de admin)
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody HistorialDTO dto) {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.insertarHistorial(dto)
        ));
    }
}
