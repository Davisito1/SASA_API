package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.DetalleMantenimientoDTO;
import APISASA.API_sasa.Services.DetalleMantenimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiDetalles")
public class ControllerDetalleMantenimiento {
    @Autowired
    private DetalleMantenimientoService service;

    @GetMapping("/consultar")
    public List<DetalleMantenimientoDTO> obtenerDetalles() {
        return service.obtenerDetalles();
    }
}
