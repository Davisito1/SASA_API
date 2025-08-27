package APISASA.API_sasa.Entities.Factura;

import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import APISASA.API_sasa.Entities.Mantenimiento.MantenimientoEntity;
import APISASA.API_sasa.Entities.Pagos.PagosEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDEMPLEADO", nullable = false)
    private EmpleadoEntity empleado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDMANTENIMIENTO", nullable = false)
    private MantenimientoEntity mantenimiento;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PagosEntity> pagos = new ArrayList<>();
}
