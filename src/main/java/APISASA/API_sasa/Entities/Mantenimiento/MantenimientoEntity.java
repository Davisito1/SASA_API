package APISASA.API_sasa.Entities.Mantenimiento;

import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
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
    @Column(name = "IDMANTENIMIENTO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "DESCRIPCIONTRABAJO", length = 500)
    private String descripcionTrabajo;

    @Column(name = "FECHAREALIZACION", nullable = false)
    private LocalDate fechaRealizacion;

    @Column(name = "CODIGOMANTENIMIENTO", unique = true, nullable = false, length = 50)
    private String codigoMantenimiento;

    // 🔹 Relación con Vehículo (FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private VehicleEntity vehiculo;
}
