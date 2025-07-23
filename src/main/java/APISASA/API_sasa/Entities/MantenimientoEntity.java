package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "MANTENIMIENTO")
@ToString @EqualsAndHashCode @Getter @Setter
public class MantenimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mantenimiento")
    @SequenceGenerator(name = "seq_mantenimiento", sequenceName = "seq_mantenimiento", allocationSize = 1)
    @Column(name = "IDMANTENIMIENTO")
    private Long id;

    @Column(name = "DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "FECHAREALIZACION", nullable = false)
    private LocalDate fechaRealizacion;

    @Column(name = "CODIGOMANTENIMIENTO", nullable = false)
    private String codigoMantenimiento;

    @Column(name = "IDVEHICULO", nullable = false)
    private Long idVehiculo;
}
