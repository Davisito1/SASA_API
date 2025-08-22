package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MANTENIMIENTO")
@ToString @EqualsAndHashCode @Getter @Setter
public class MantenimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mantenimiento")
    @SequenceGenerator(name = "seq_mantenimiento", sequenceName = "seq_mantenimiento", allocationSize = 1)
    @Column(name = "IDMANTENIMIENTO")
    private Long id;

    @Column(name = "DESCRIPCIONTRABAJO", nullable = true) // puede ser false si la haces obligatoria luego
    private String descripcion;

    @Column(name = "FECHAREALIZACION", nullable = false)
    private LocalDate fechaRealizacion;

    @Column(name = "CODIGOMANTENIMIENTO", nullable = false)
    private String codigoMantenimiento;

    @ManyToOne
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private VehicleEntity idVehiculo;

    @OneToMany(mappedBy = "idMantenimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleMantenimientoEntity> detalles = new ArrayList<>();
}
