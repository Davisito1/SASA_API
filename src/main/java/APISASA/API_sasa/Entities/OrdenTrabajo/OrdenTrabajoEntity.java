package APISASA.API_sasa.Entities.OrdenTrabajo;

import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import APISASA.API_sasa.Entities.DetalleOrden.DetalleOrdenEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORDENTRABAJO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenTrabajoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orden_seq")
    @SequenceGenerator(name = "orden_seq", sequenceName = "SEQ_ORDENTRABAJO", allocationSize = 1)
    @Column(name = "IDORDEN")
    private Long idOrden;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    // 🔹 Relación con Vehículo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDVEHICULO", nullable = false)
    private VehicleEntity vehiculo;

    // 🔹 Relación con Detalles (1 orden tiene muchos detalles)
    @OneToMany(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleOrdenEntity> detalles = new ArrayList<>();
}
