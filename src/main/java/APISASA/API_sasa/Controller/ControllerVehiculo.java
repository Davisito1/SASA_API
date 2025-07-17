package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.VehicleDTO;
import APISASA.API_sasa.Services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiVehiculo")
public class ControllerVehiculo {

    @Autowired
    private VehicleService service;

    //Consulta de datos
    @GetMapping ("/consultarVehiculos")
    public List <VehicleDTO> obtenerVehiculos(){
        return service.obtenerVehiculos();
    }
}
