package APISASA.API_sasa.Models.DTO.DetalleOrden;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrdenDTO {

    private Long id; // idDetalle

    @NotNull(message = "Debe asignarse una orden de trabajo")
    @Min(value = 1, message = "El idOrden debe ser mayor a 0")
    private Long idOrden;

    @NotNull(message = "Debe asignarse un mantenimiento válido")
    @Min(value = 1, message = "El idMantenimiento debe ser mayor a 0")
    private Long idMantenimiento;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio unitario debe ser mayor a 0")
    private Double precioUnitario;

    private Double subtotal; // puede calcularse en el service = cantidad * precioUnitario
}
