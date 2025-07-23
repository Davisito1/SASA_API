package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "HISTORIALVEHICULO")
@ToString @EqualsAndHashCode @Getter @Setter
public class HistorialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_historialVehiculo")
    @SequenceGenerator(
            name = "seq_historialVehiculo",
            sequenceName = "SEQ_HISTORIALVEHICULO", // <- Este debe coincidir con Oracle
            allocationSize = 1
    )
    @Column(name = "IDHISTORIAL", insertable = false, updatable = false)
    private Long id;

    @Column(name = "FECHAINGRESO", nullable = false)
    private LocalDate fechaIngreso;

    @Column(name = "FECHASALIDA", nullable = false)
    private LocalDate fechaSalida;

    @Column(name = "TRABAJOREALIZADO", nullable = false)
    private String trabajoRealizado;

    @Column(name = "OBSERVACIONES")
    private String observaciones;

    @Column(name = "IDVEHICULO", nullable = false)
    private Long idVehiculo;
}
