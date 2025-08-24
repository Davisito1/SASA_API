package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DETALLEMANTENIMIENTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleMantenimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalleMantenimiento_seq")
    @SequenceGenerator(
            name = "detalleMantenimiento_seq",
            sequenceName = "SEQ_DETALLEMANTENIMIENTO",
            allocationSize = 1
    )
    @Column(name = "IDDETALLEMANTENIMIENTO")
    private Long idDetalleMantenimiento;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMANTENIMIENTO", nullable = false)
    private MantenimientoEntity mantenimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDSERVICIO", nullable = false)
    private ServicioEntity servicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDTIPOMANTENIMIENTO", nullable = false)
    private TipoMantenimientoEntity tipoMantenimiento;
}