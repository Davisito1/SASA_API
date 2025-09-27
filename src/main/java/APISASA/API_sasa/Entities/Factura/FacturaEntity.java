package APISASA.API_sasa.Entities.Factura;

import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import APISASA.API_sasa.Entities.OrdenTrabajo.OrdenTrabajoEntity;
import APISASA.API_sasa.Entities.MetodoPago.MetodoPagoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "FACTURA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacturaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "factura_seq")
    @SequenceGenerator(name = "factura_seq", sequenceName = "SEQ_FACTURA", allocationSize = 1)
    @Column(name = "IDFACTURA")
    private Long idFactura;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "MONTOTOTAL", nullable = false)
    private Double montoTotal;

    @Column(name = "ESTADO", nullable = false, length = 20)
    private String estado; // Pendiente, Pagada, Cancelada

    @Column(name = "REFERENCIAPAGO", length = 100)
    private String referenciaPago;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    // 🔹 Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDEMPLEADO", nullable = false)
    private EmpleadoEntity empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDORDEN", nullable = false)
    private OrdenTrabajoEntity ordenTrabajo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMETODOPAGO", nullable = false)
    private MetodoPagoEntity metodoPago;
}
