package APISASA.API_sasa.Entities.Historial;

import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "HISTORIALVEHICULO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class HistorialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "historial_seq")
    @SequenceGenerator(
            name = "historial_seq",
            sequenceName = "SEQ_HISTORIALVEHICULO",
            allocationSize = 1
    )
    @Column(name = "IDHISTORIAL", insertable = false, updatable = false)
    private Long idHistorial;

    @Column(name = "FECHAINGRESO", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "FECHASALIDA")
    private LocalDate fechaSalida;

    @Column(name = "TRABAJOREALIZADO", length = 500)
    private String trabajoRealizado;

    @Column(name = "OBSERVACIONES", length = 500)
    private String observaciones;

    //  Relación con Vehículo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private VehicleEntity vehiculo;
}
