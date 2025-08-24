package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DETALLEMANTENIMIENTO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class DetalleMantenimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "detalleMantenimiento_seq")
    @SequenceGenerator(
            name = "detalleMantenimiento_seq",
            sequenceName = "SEQ_DETALLEMANTENIMIENTO",  // Mayúsculas exactas
            allocationSize = 1
    )
    @Column(name = "IDDETALLEMANTENIMIENTO")
    private Long idDetalleMantenimiento;

    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMANTENIMIENTO", nullable = false)
    private MantenimientoEntity idMantenimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDSERVICIO", nullable = false)
    private ServicioEntity idServicio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDTIPOMANTENIMIENTO", nullable = false)
    private TipoMantenimientoEntity idTipoMantenimiento;
}
