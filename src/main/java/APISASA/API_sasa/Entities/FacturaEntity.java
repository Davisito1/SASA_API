package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "FACTURA")
@ToString @EqualsAndHashCode @Getter @Setter
public class FacturaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_factura")
    @SequenceGenerator(name = "seq_factura", sequenceName = "seq_factura", allocationSize = 1)
    @Column(name = "IDFACTURA", insertable = false, updatable = false)
    private Long id;

    @Column(name = "FECHA", nullable = false)
    private LocalDate fecha;

    @Column(name = "MONTOTOTAL", nullable = false)
    private double montoTotal;

    @Column(name = "IDEMPLEADO", nullable = false)
    private Long idEmpleado;

    @Column(name = "IDMANTENIMIENTO", nullable = false)
    private Long idMantenimiento;
}
