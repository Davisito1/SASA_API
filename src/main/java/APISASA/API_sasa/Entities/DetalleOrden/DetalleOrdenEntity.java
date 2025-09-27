package APISASA.API_sasa.Entities.DetalleOrden;

import APISASA.API_sasa.Entities.Mantenimiento.MantenimientoEntity;
import APISASA.API_sasa.Entities.OrdenTrabajo.OrdenTrabajoEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DETALLEORDEN")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleOrdenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalle_seq")
    @SequenceGenerator(
            name = "detalle_seq",
            sequenceName = "SEQ_DETALLERODEN",
            allocationSize = 1
    )
    @Column(name = "IDDETALLE")
    private Long idDetalle;

    // 🔹 Relación con Orden de Trabajo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDORDEN", nullable = false)
    private OrdenTrabajoEntity ordenTrabajo;

    // 🔹 Relación con Mantenimiento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMANTENIMIENTO", nullable = false)
    private MantenimientoEntity mantenimiento;

    @Column(name = "CANTIDAD", nullable = false)
    private Integer cantidad;

    @Column(name = "PRECIOUNITARIO", nullable = false)
    private Double precioUnitario;

    // 🔹 Campo faltante: Subtotal
    @Column(name = "SUBTOTAL")
    private Double subtotal;
}
