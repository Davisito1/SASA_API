package APISASA.API_sasa.Models.DTO.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserDTO {

    private Long id;


    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String nombreUsuario;


    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El formato del correo no es válido")
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String contrasena;

    @NotBlank(message = "El rol no puede estar vacío")
    private String rol;

    @NotNull(message = "El estado no puede ser nulo")
    private String estado;
}
