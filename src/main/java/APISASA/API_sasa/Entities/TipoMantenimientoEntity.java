package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table()
@ToString @EqualsAndHashCode @Getter @Setter
public class TipoMantenimientoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tipoMantenimiento")
    @SequenceGenerator(name = "seq_tipoMantenimiento", sequenceName = "seq_tipoMantenimiento", allocationSize = 1)
    @Column(name = "IDTIPOMANTENIMIENTO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "TIPOMANTENIMIENTO", nullable = false)
    private String tipoMantenimiento;
}
