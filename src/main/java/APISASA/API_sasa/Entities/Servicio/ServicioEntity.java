package APISASA.API_sasa.Entities.Servicio;

import APISASA.API_sasa.Entities.DetalleMantenimiento.DetalleMantenimientoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SERVICIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "servicio_seq")
    @SequenceGenerator(name = "servicio_seq", sequenceName = "SEQ_SERVICIO", allocationSize = 1)
    @Column(name = "IDSERVICIO")
    private Long idServicio;

    @Column(name = "NOMBRESERVICIO", nullable = false, length = 100)
    private String nombreServicio;

    @Column(name = "DESCRIPCION", nullable = false, length = 500)
    private String descripcion;

    @Column(name = "PRECIO", nullable = false)
    private Double precio;

    @Column(name = "DURACIONESTIMADA", length = 50)
    private String duracionEstimada;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleMantenimientoEntity> detalles = new ArrayList<>();
}
