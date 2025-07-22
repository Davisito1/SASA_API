package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class EstadoDTO {

    private Long id;

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(max = 50, message = "El nombre del estado no puede exceder 50 caracteres")
    private String nombreEstado;
}
