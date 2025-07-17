package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "ESTADOVEHICULO")
@ToString
@EqualsAndHashCode
@Getter @Setter
public class EstadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_estadoVehiculo")
    @SequenceGenerator(name = "seq_estadoVehiculo", sequenceName = "seq_estadoVehiculo", allocationSize = 1)
    @Column(name = "IDESTADO", insertable = false, updatable = false)
    private long id;
    @Column(name = "NOMBREESTADO")
    private String nombreEstado;
}
