package APISASA.API_sasa.Models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class UserDTO {

    //Atributos
    private long id;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombreUsuario;
    @Email (message = "Debe ser un correo valido")
    private String contrasena;
    @Size (min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String rol;
    @NotNull (message = "el rol no debe de ser nulo")
    private String estado;
}
