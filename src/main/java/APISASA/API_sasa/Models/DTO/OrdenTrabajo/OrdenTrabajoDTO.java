package APISASA.API_sasa.Models.DTO.OrdenTrabajo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenTrabajoDTO {

    private Long id; // idOrden

    @NotNull(message = "Debe asignarse un vehículo válido")
    @Min(value = 1, message = "El idVehiculo debe ser mayor a 0")
    private Long idVehiculo;

    @NotNull(message = "La fecha de la orden es obligatoria")
    @PastOrPresent(message = "La fecha de la orden no puede ser futura")
    private LocalDate fecha;

    // 🔹 Lista de detalles (opcional, se puede cargar desde el service)
    private List<Long> detallesIds;
}
