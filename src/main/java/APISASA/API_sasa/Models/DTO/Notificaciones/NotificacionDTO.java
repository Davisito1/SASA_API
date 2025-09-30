package APISASA.API_sasa.Models.DTO.Notificaciones;

import jakarta.validation.constraints.*;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionDTO {
    private Long id;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotBlank(message = "Debe indicarse el tipo de notificación")
    @Pattern(regexp = "^(Informativa|Alerta|Urgente)$",
            message = "Tipo de notificación inválido (Informativa, Alerta, Urgente)")
    private String tipoNotificacion;

    @NotBlank(message = "Debe indicarse la prioridad")
    private String prioridad;

    @NotNull(message = "Debe asignarse un usuario válido")
    @Min(value = 1, message = "Debe asignarse un usuario válido")
    private Long idUsuario;
}
