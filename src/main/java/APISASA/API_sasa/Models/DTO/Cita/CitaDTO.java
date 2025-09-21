package APISASA.API_sasa.Models.DTO.Cita;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class CitaDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha de la cita debe ser hoy o en el futuro")
    private LocalDate fecha;

    @NotBlank(message = "La hora es obligatoria")
    @Pattern(
            regexp = "^(0?[1-9]|1[0-2]):[0-5][0-9]\\s?(AM|PM)$",
            message = "La hora debe estar en formato hh:mm AM/PM (ej. 07:30 AM, 3:45 PM)"
    )
    private String hora;

    @NotBlank(message = "El estado de la cita es obligatorio")
    @Pattern(
            regexp = "^(Pendiente|Confirmada|Cancelada|Completada)$",
            message = "El estado debe ser Pendiente, Confirmada, Cancelada o Completada"
    )
    private String estado;

    @NotNull(message = "El cliente es obligatorio")
    @Min(value = 1, message = "Debe asignarse un cliente válido")
    private Long idCliente;

    // 👇 Campo adicional, no está en la BD
    private String clienteNombre;

    @NotNull(message = "El vehículo es obligatorio")
    @Min(value = 1, message = "Debe asignarse un vehículo válido")
    private Long idVehiculo;

    // 👇 Campo adicional, no está en la BD
    private String vehiculoNombre;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String descripcion;

    @NotBlank(message = "El tipo de servicio es obligatorio")
    @Pattern(
            regexp = "^(Mantenimiento preventivo|Mantenimiento correctivo)$",
            message = "El tipo de servicio debe ser Mantenimiento preventivo o Mantenimiento correctivo"
    )
    private String tipoServicio;
}
