package APISASA.API_sasa.Models.DTO.Pagos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class PagosDTO {
    private Long id;

    @NotNull(message = "Fecha de pago obligatoria")
    private LocalDate fecha;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    private double monto;

    @NotNull(message = "Debe seleccionar un método de pago")
    private Long idMetodoPago;

    @NotNull(message = "Debe seleccionar una factura")
    @Min(value = 1, message = "Debe asignarse una factura válida")
    private Long idFactura;
}
