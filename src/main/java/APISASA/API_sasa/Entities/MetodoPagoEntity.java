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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_metodo_pago")
    @SequenceGenerator(
            name = "seq_metodo_pago",
            sequenceName = "SEQ_METODOPAGO",
            allocationSize = 1
    )
    @Column(name = "IDMETODOPAGO", insertable = false, updatable = false)
    private Long id;


    @Column(name = "METODO")
    private String metodo;
}
