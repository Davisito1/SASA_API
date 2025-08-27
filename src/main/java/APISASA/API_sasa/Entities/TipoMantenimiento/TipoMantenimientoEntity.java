package APISASA.API_sasa.Entities.TipoMantenimiento;

import APISASA.API_sasa.Entities.DetalleMantenimiento.DetalleMantenimientoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TIPOMANTENIMIENTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoMantenimientoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoMantenimiento_seq")
    @SequenceGenerator(name = "tipoMantenimiento_seq", sequenceName = "SEQ_TIPOMANTENIMIENTO", allocationSize = 1)
    @Column(name = "IDTIPOMANTENIMIENTO")
    private Long idTipoMantenimiento;

    @Column(name = "TIPOMANTENIMIENTO", nullable = false, length = 100)
    private String tipoMantenimiento;

    @OneToMany(mappedBy = "tipoMantenimiento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleMantenimientoEntity> detalles = new ArrayList<>();
}
