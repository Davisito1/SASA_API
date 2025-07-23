package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.HistorialDTO;
import APISASA.API_sasa.Services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiHistorial")
public class ControllerHistorial {
    @Autowired
    private HistorialService service;

    @GetMapping("/consultar")
    public List<HistorialDTO> obtenerHistorial() {
        return service.obtenerHistorial();
    }
}
