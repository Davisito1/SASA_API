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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "estado_seq")
    @SequenceGenerator(name = "estado_seq", sequenceName = "SEQ_ESTADOVEHICULO", allocationSize = 1)
    @Column(name = "IDESTADO")
    private Long id;


    @Column(name = "NOMBREESTADO")
    private String nombreEstado;
}
