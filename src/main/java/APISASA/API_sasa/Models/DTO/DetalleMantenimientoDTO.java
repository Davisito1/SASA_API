package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class DetalleMantenimientoDTO {

    private Long id;

    @NotBlank(message = "Estado obligatorio")
    private String estado;

    @NotNull(message = "Debe asignarse un mantenimiento válido")
    @Min(value = 1, message = "ID de mantenimiento inválido")
    private Long idMantenimiento;

    @NotNull(message = "Debe asignarse un servicio válido")
    @Min(value = 1, message = "ID de servicio inválido")
    private Long idServicio;

    @NotNull(message = "Debe asignarse un tipo de mantenimiento válido")
    @Min(value = 1, message = "ID de tipo de mantenimiento inválido")
    private Long idTipoMantenimiento;
}
