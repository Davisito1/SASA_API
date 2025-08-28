package APISASA.API_sasa.Models.DTO.Cita;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class CitaDTO {

    private Long id;

    @NotNull(message = "La decha es obligatoria")
    @FutureOrPresent(message = "La fecha de la cita debe ser hoy o en el futuro")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private String hora;

    @NotNull(message = "El estado de la cita es olbigatorio")
    private String estado;

    @NotNull(message = "El cliente es obligatorio")
    @Min(value = 1, message = "Debe asignarse un cliente válido")
    private Long idCliente;
}
