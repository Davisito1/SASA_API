package APISASA.API_sasa.Models.DTO;

import APISASA.API_sasa.Entities.ClienteEntity;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class CitaDTO {

    private Long id;

    @NotNull(message = "La decha es obligatoria")
    @Future(message = "La fecha de la cita no puede ser del pasado")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private String hora;

    @NotNull(message = "El estado de la cita es olbigatorio")
    private String estado;

    @NotNull(message = "El cliente es obligatorio")
    @Min(value = 1, message = "Debe asignarse un cliente válido")
    private ClienteEntity idCliente;
}
