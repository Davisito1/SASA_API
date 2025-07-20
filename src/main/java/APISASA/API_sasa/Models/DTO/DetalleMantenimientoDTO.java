package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class DetalleMantenimientoDTO {
    private Long id;

    @NotBlank(message = "Estado obligatorio")
    private String estado;

    @Min(value = 1, message = "Debe asignarse un mantenimiento valido")
    private Long idMantenimiento;

    @Min(value = 1, message = "Debe asignarse un servicio valido")
    private Long idServicio;

    @Min(value =1, message = "Debe asignarse un tipo de mantenimiento valido")
    private Long idTipoMantenimiento;
}
