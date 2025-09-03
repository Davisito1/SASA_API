package APISASA.API_sasa.Models.DTO.Cita;

import jakarta.validation.constraints.*;
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
    @Pattern(
            regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$",
            message = "La hora debe estar en formato HH:mm (24 horas)."
    )
    private String hora;

    @NotNull(message = "El estado de la cita es olbigatorio")
    private String estado;

    @NotNull(message = "El cliente es obligatorio")
    @Min(value = 1, message = "Debe asignarse un cliente válido")
    private Long idCliente;
}
