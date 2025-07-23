package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class TipoMantenimientoDTO {
    private Long id;

    @NotBlank(message = "Tipo de mantenimiento requerido")
    private String tipoMantenimiento;
}
