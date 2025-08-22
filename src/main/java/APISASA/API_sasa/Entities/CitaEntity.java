package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Past;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "CITA")
@ToString @EqualsAndHashCode
@Getter @Setter
public class CitaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita")
    @SequenceGenerator(name = "seq_cita", sequenceName = "seq_cita", allocationSize = 1)
    @Column(name = "IDCITA", insertable = false, updatable = false)
    private Long id;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "HORA", nullable = false)
    private String hora;

    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @ManyToOne
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private ClienteEntity idCliente;
}
