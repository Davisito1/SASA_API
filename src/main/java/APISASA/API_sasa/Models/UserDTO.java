package APISASA.API_sasa.Models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class UserDTO {
    private long id;
    private String nombreUsuario;
    private String contrasena;
    private String rol;
    private String estado;
}
