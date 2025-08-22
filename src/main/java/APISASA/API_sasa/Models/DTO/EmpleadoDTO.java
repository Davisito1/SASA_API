package APISASA.API_sasa.Models.DTO;

import APISASA.API_sasa.Entities.UserEntity;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class EmpleadoDTO {
    private Long id;

    @NotBlank(message = "El nombre olbigatorio")
    @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido no debe exceder 100 caracteres")
    private String apellidos;

    @NotBlank(message = "Cargo obligatorio")
    private String cargo;

    @NotBlank(message = "El DUI es obligatorio")
    @Pattern(regexp = "^\\d{8}-\\d{1}$", message = "El DUI debe tener el formato ########-#")
    private String dui;

    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "El número de teléfono debe tener un formato ####-####")
    private String telefono;

    @NotBlank(message = "La dirección es olbigatoria")
    private String direccion;

    @NotNull(message = "Fecha de contratación obligatoria")
    private LocalDate fechaContratacion;

    @NotNull(message = "Correo electrónico obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Size(max = 100, message = "El correo eléctronico no puede exceder 100 craracteres")
    private String correo;

    @Min(value = 1, message = "Debe asignarse un usuario válido")
    private UserEntity idUsuario;
}
