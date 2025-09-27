package APISASA.API_sasa.Models.DTO.Factura;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class FacturaDTO {
    private Long id;

    @NotNull(message = "La fecha de emisión es obligatoria")
    @PastOrPresent(message = "La fecha de emisión no puede ser futura")
    private LocalDate fecha;

    @NotNull(message = "El monto total es obligatorio")
    @PositiveOrZero(message = "El monto no puede ser negativo")
    private Double montoTotal;

    @NotNull(message = "Debe asignarse un empleado válido")
    @Min(value = 1, message = "El idEmpleado debe ser mayor a 0")
    private Long idEmpleado;

    @NotNull(message = "Debe asignarse una orden de trabajo válida")
    @Min(value = 1, message = "El idOrden debe ser mayor a 0")
    private Long idOrden;

    @NotNull(message = "Debe especificarse el método de pago")
    @Min(value = 1, message = "El idMetodoPago debe ser mayor a 0")
    private Long idMetodoPago;

    @Size(max = 100, message = "La referencia de pago no debe superar los 100 caracteres")
    private String referenciaPago;

    @Size(max = 255, message = "La descripción no debe superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El estado de la factura es obligatorio")
    @Pattern(regexp = "Pendiente|Pagada|Cancelada", message = "Estado inválido")
    private String estado;
}
