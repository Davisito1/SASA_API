package APISASA.API_sasa.Entities.Estado;

import APISASA.API_sasa.Entities.Vehiculo.VehicleEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ESTADOVEHICULO")
@Getter @Setter
@ToString @EqualsAndHashCode
public class EstadoVehiculoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estadoVehiculo")
    @SequenceGenerator(name = "seq_estadoVehiculo", sequenceName = "seq_estadoVehiculo", allocationSize = 1)
    @Column(name = "IDESTADO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "NOMBREESTADO", nullable = false, unique = true, length = 50)
    private String nombreEstado;

    @OneToMany(mappedBy = "estado", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<VehicleEntity> vehiculos;
}
