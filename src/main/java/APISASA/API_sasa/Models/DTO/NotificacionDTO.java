package APISASA.API_sasa.Models.DTO;

import APISASA.API_sasa.Entities.UserEntity;
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

    @Size(min = 0, max = 1)
    private Long lectura;

    @NotBlank(message = "Debe indicarse el tipo de propiedad")
    private String prioridad;

    @Min(value = 1, message = "Debe asignarse un usuario valido")
    private UserEntity idUsuario;
}
