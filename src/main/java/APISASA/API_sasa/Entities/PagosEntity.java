package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "PAGO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pago_seq")
    @SequenceGenerator(name = "pago_seq", sequenceName = "SEQ_PAGO", allocationSize = 1)
    @Column(name = "IDPAGO")
    private Long idPago;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "MONTO", nullable = false)
    private Double monto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDFACTURA", nullable = false)
    private FacturaEntity factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMETODOPAGO", nullable = false)
    private MetodoPagoEntity metodoPago;
}
