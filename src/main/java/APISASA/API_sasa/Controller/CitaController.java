package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Services.CitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiCitas")
public class CitaController {
    @Autowired
    private CitaService service;

    @GetMapping("/consultar")
    public List<CitaDTO> obtenerCitas() {
        return service.obtenerCitas();
    }
}
