package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

import java.time.LocalDate;

@Entity
@Table(name = "NOTIFICACION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificacion_seq")
    @SequenceGenerator(name = "notificacion_seq", sequenceName = "SEQ_NOTIFICACION", allocationSize = 1)
    @Column(name = "IDNOTIFICACION")
    private Long idNotificacion;

    @Column(name = "MENSAJE", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "TIPONOTIFICACION", nullable = false, length = 50)
    private String tipoNotificacion;

    @Column(name = "LECTURA", nullable = false)
    private Integer lectura;

    @Column(name = "PRIORIDAD", length = 20)
    private String prioridad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO", nullable = false)
    private UserEntity usuario;
}
