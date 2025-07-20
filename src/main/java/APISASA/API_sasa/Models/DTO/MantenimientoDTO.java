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

    @NotBlank(message = "Descripcion obligatoria")
    @Size(max = 500, message = "La descipción no puede acceder de mas de 500 caracteres")
    private String descripcion;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaRealizacion;

    @NotNull(message = "El codigo es obligatorio")
    @Size(max = 50)
    private String codigoMantenimiento;

    @Min(value = 1, message = "Debe asignarse un vehículo válido")
    private Long idVehiculo;
}
