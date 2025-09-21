package APISASA.API_sasa.Entities.Cita;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
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

    // ==========================
    // FECHA
    // ==========================
    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    // ==========================
    // HORA
    // ==========================
    @Column(name = "HORA", nullable = false, length = 20) // ampliado para hh:mm AM/PM
    private String hora;

    // ==========================
    // ESTADO
    // ==========================
    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    // ==========================
    // CLIENTE
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private ClienteEntity cliente;

    // ==========================
    // VEHÍCULO
    // ==========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private VehicleEntity vehiculo;

    // ==========================
    // DESCRIPCIÓN (opcional)
    // ==========================
    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "TIPOSERVICIO", length = 50, nullable = false)
    private String tipoServicio;
}
