package APISASA.API_sasa.Entities.Cita;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "CITA")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CitaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cita")
    @SequenceGenerator(name = "seq_cita", sequenceName = "seq_cita", allocationSize = 1)
    @Column(name = "IDCITA", insertable = false, updatable = false)
    private Long id;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "HORA", nullable = false, length = 10)
    private String hora;

    @Column(name = "ESTADO", nullable = false, length = 50)
    private String estado;

    // Relación con Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private ClienteEntity cliente;
}
