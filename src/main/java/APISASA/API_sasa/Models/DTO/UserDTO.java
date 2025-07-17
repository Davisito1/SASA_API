package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter @Setter @ToString @EqualsAndHashCode
public class UserDTO {
    private long id;
    @NotBlank
    private String nombreUsuario;
    @NotBlank
    private String contrasena;
    @Size (min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String rol;
    @NotNull (message = "El rol no puede ser nulo")
    private String estado;

}
