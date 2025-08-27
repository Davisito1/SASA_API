package APISASA.API_sasa.Controller.MetodoPago;

import APISASA.API_sasa.Services.MetodoPago.MetodoPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/metodoPago")
@CrossOrigin
public class ControllerMetodoPago {

    @Autowired
    private MetodoPagoService service;

    //Consultar todos los métodos de pago (sin paginación)
    @GetMapping("/listar")
    public ResponseEntity<?> listarMetodos() {
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "data", service.obtenerTodos()
        ));
    }
}
