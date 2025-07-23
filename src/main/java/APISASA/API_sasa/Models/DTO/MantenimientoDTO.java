package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaRealizacion;

    @NotNull(message = "El código es obligatorio")
    @Size(max = 50, message = "El código no puede exceder los 50 caracteres")
    private String codigoMantenimiento;

    @NotNull(message = "Vehículo obligatorio")
    @Min(value = 1, message = "Debe asignarse un vehículo válido")
    private Long idVehiculo;
}
