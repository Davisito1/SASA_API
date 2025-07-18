package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.DTO.ClientDTO;
import APISASA.API_sasa.Services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiCliente")
public class ControllerCliente {
    @Autowired
    private ClienteService acceso;

    @GetMapping("/clientes")
    public List<ClientDTO> datosClientes() {
        return acceso.getAllClients();
    };
}
