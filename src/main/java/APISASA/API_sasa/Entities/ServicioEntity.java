package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SERVICIO")
@ToString @EqualsAndHashCode @Getter @Setter
public class ServicioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_servicio")
    @SequenceGenerator(name = "seq_servicio", sequenceName = "seq_servicio", allocationSize = 1)
    @Column(name = "IDSERVICIO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "NOMBRESERVICIO", nullable = false)
    private String nombreServicio;

    @Column(name = "DESCRIPCION", nullable = false)
    private String descripcion;

    @Column(name = "PRECIO", nullable = false)
    private double precio;

    @Column(name = "DURACIONESTIMADA", nullable = false)
    private String duracion;

    @OneToMany(mappedBy = "idServicio")
    private List<DetalleMantenimientoEntity> detalles = new ArrayList<>();
}
