package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ServicioDTO {
    private Long id;

    @NotBlank(message = "Nombre del servicio obligatorio")
    private String nombre;

    @NotBlank(message = "Descripción obligatoria")
    private String descripcion;

    @NotBlank(message = "Precio obligatorio")
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private double precio;

    @NotBlank(message = "Debe asignarse la duracion del servicio")
    private String duracion;
}
