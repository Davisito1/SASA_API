package APISASA.API_sasa.Models.DTO;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ClientDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no debe exceder 100 caracteres")
    private String apellido;

    @NotBlank(message = "El DUI es obligatorio")
    @Pattern(regexp = "^\\d{8}-\\d{1}$", message = "El DUI debe tener el formato ########-#")
    private String dui;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El género es obligatorio")
    @Pattern(regexp = "^(Masculino|Femenino|Otro)$", message = "Género inválido (Masculino, Femenino u Otro)")
    private String genero;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Size(max = 150, message = "El correo no debe exceder 150 caracteres")
    private String correoElectronico;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    private String contrasena;
}
