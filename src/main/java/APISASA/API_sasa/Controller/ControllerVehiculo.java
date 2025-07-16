package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/apiVehiculo")
public class ControllerVehiculo {
    @Autowired
    public VehicleService acceso;
}
