package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Services.TipoMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/tipoMantenimiento")
public class ControllerTipoMantenimiento {

    @Autowired
    private TipoMantenimientoService service;

    // 🔹 Consultar todos los tipos (catálogo)
    @GetMapping("/listar")
    public ResponseEntity<?> obtenerTipos() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerTipos()
        ));
    }
}
