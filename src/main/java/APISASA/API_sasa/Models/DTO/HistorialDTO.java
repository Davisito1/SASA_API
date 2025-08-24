package APISASA.API_sasa.Models.DTO;

import APISASA.API_sasa.Entities.VehicleEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class HistorialDTO {
    private Long id;

    @NotNull(message = "Fecha de ingreso obligatoria")
    private LocalDate fechaIngreso;

    @NotNull(message = "Fecha de salida olbigatoria")
    private LocalDate fechaSalida;

    private String trabajoRealizado;
    private String observaciones;

    @Min(value = 1, message = "Debe asignarse un vehiculo valido")
    private Long idVehiculo;
}
