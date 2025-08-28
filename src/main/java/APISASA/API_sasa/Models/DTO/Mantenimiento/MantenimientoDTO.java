package APISASA.API_sasa.Models.DTO.Mantenimiento;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class MantenimientoDTO {
    private Long id;

    @NotBlank(message = "Descripción obligatoria")
    @Size(max = 500, message = "La descripción no puede exceder los 500 caracteres")
    @Pattern(regexp = ".*[a-zA-Z].*", message = "La descripción no puede ser solo números")
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaRealizacion;

    @NotNull(message = "El código es obligatorio")
    private String codigoMantenimiento;

    @NotNull(message = "Vehículo obligatorio")
    @Min(value = 1, message = "Debe asignarse un vehículo válido")
    private Long idVehiculo;
}
