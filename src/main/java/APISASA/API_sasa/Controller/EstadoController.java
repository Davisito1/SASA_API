package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.EstadoDTO;
import APISASA.API_sasa.Services.EstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiEstado")
public class EstadoController {
    @Autowired
    private EstadoService acceso;

    @GetMapping("/estado")
    public List<EstadoDTO> datosEstado(){
        return acceso.getAllStatus();
    }
}
