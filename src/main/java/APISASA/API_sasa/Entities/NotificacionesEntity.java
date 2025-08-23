package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "NOTIFICACION")
@ToString @EqualsAndHashCode @Getter @Setter
public class NotificacionesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notificacion")
    @SequenceGenerator(name = "seq_notificacion", sequenceName = "seq_notificacion", allocationSize = 1)
    @Column(name = "IDNOTIFICACION", insertable = false, updatable = false)
    private Long id;

    @Column(name = "MENSAJE", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "TIPONOTIFICACION", nullable = false, length = 50)
    private String tipoNotificacion;

    // 👇 Correcto para NUMBER(1)
    @Column(name = "LECTURA", nullable = false)
    private Integer lectura = 0;

    @Column(name = "PRIORIDAD", length = 20)
    private String prioridad;

    @Column(name = "IDUSUARIO", nullable = false)
    private Long idUsuario;
}
