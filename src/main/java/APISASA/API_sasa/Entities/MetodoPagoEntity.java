package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "METODOPAGO")
@ToString @EqualsAndHashCode @Getter @Setter
public class MetodoPagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_metodoPago")
    @SequenceGenerator(name = "seq_metodoPago", sequenceName = "seq_metodoPago", allocationSize = 1)
    @Column(name = "IDMETODOPAGO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "METODO")
    private String metodo;
}
