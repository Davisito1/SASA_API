package APISASA.API_sasa.Models.DTO.Empleado;

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

    private Long id;  // 👈 lo dejamos así como pediste

      private Long idUsuario;        // id del usuario asociado
    private String nombreUsuario;  // nombre de usuario asociado (para mostrar en tabla)

    @NotBlank(message = "El nombre es obligatorio")
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

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "El número de teléfono debe tener un formato ####-####")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotNull(message = "Fecha de contratación obligatoria")
    private LocalDate fechaContratacion;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser una fecha pasada")
    private LocalDate fechaNacimiento;

    @NotNull(message = "Correo electrónico obligatorio")
    @Email(message = "Formato de correo electrónico inválido")
    @Size(max = 100, message = "El correo electrónico no puede exceder 100 caracteres")
    private String correo;

    // 🔹 Datos para crear Usuario automáticamente
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String contrasena;
}
