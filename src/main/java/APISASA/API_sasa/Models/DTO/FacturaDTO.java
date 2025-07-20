package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class FacturaDTO {
    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El monto es obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private double montoTotal;

    @Min(value = 1, message = "Debe asignarse un empleado valido")
    private Long idEmpleado;

    @Min(value = 1, message = "Debe asignarse un mantenimiento valido")
    private Long idMantenimiento;
}
