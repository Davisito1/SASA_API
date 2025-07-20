package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "DETALLEMANTENIMIENTO")
@Getter @Setter @ToString @EqualsAndHashCode
public class DetalleMantenimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_detalleMantenimiento")
    @SequenceGenerator(name = "seq_detalleMantenimiento", sequenceName = "seq_detalleMantenimiento", allocationSize = 1)
    @Column(name = "IDDETALLEMANTENIMIENTO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "ESTADO", nullable = false)
    private String estado;

    @Column(name = "IDMANTENIMIENTO")
    private Long idMantenimiento;

    @Column(name = "IDSERVICIO")
    private Long idServicio;

    @Column(name = "IDTIPOMANTENIMIENTO")
    private Long idTipoMantenimiento;
}
