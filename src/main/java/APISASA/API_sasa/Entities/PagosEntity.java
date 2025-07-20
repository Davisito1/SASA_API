package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "PAGO")
@ToString @EqualsAndHashCode @Getter @Setter
public class PagosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pago")
    @SequenceGenerator(name = "seq_pago", sequenceName = "seq_pago", allocationSize = 1)
    @Column(name = "IDPAGO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "MONTO", nullable = false)
    private double monto;

    @Column(name = "IDMETODOPAGO", nullable = false)
    private Long idMetodoPago;

    @Column(name = "IDFACTURA", nullable = false)
    private Long idFactura;
}
