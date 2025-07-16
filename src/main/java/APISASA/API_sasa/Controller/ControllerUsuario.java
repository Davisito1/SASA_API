package APISASA.API_sasa.Controller;

import APISASA.API_sasa.Models.UserDTO;
import APISASA.API_sasa.Models.UserDTO;
import APISASA.API_sasa.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/apiUsuario")
public class ControllerUsuario {
    @Autowired
     UserService acceso;
//localhost:8080/apiUsuario/datosUsuarios
    @GetMapping("/datosUsuarios")
    public List<UserDTO> datosUsuarios(){
        return acceso.getAllUsers();
    }
}
