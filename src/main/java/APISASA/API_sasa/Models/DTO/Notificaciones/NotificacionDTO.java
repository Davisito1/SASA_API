package APISASA.API_sasa.Models.DTO.Notificaciones;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class NotificacionDTO {
    private Long id;

    @NotBlank(message = "El mensaje es olbigatorio")
    private String mensaje;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "Debe indicarse el tipo de notificación")
    @Pattern(regexp = "^(Informativa|Alerta|Urgenta)$", message = "Tipo de notificacion invalido (Informativa, Alerta y Urgente)")
    private String tipoNotificacion;

    @NotNull(message = "El estado de lectura es obligatorio")
    @Min(value = 0, message = "El valor mínimo de lectura es 0")
    @Max(value = 1, message = "El valor máximo de lectura es 1")
    private Integer lectura;

    @NotBlank(message = "Debe indicarse el tipo de propiedad")
    private String prioridad;

    @Min(value = 1, message = "Debe asignarse un usuario valido")
    private Long idUsuario;
}
