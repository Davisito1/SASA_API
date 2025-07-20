package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.EmpleadoDTO;
import APISASA.API_sasa.Services.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiEmpleados")
public class ControllerEmpleado {
    @Autowired
    private EmpleadoService service;

    @GetMapping("/consultar")
    public List<EmpleadoDTO> obtenerEmpleados() {
        return service.obtenerEmpleados();
    }
}
