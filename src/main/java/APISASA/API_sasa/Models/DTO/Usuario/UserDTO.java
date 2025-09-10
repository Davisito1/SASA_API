package APISASA.API_sasa.Models.DTO.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    // Opcional para login (puedes usar correo o usuario)
    private String nombreUsuario;

    // Opcional si usas nombreUsuario
    @Email(message = "El formato del correo no es válido")
    private String correo;

    // Siempre obligatorio en login y registro
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contraseña debe contener al menos 8 caracteres")
    private String contrasena;

    private String rol;

    private String estado;
}
