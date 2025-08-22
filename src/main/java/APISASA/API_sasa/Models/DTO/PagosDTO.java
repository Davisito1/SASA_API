package APISASA.API_sasa.Models.DTO;

import APISASA.API_sasa.Entities.FacturaEntity;
import APISASA.API_sasa.Entities.MetodoPagoEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
    @PositiveOrZero(message = "El precio no puede ser negativo")
    private double monto;

    @Min(value = 1, message = "Debe asignarse un método de pago valido")
    private MetodoPagoEntity metodoPago;

    @Min(value = 1, message = "Debe asignarse una factura valida")
    private FacturaEntity idFactura;
}
