package APISASA.API_sasa.Models.DTO.MetodoPago;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class MetodoPagoDTO {
    private Long id;

    @NotNull(message = "Método requerido")
    private String metodo;
}
