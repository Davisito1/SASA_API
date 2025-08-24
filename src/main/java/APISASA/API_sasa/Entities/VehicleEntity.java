package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Vehiculo")
@Getter @Setter @ToString @EqualsAndHashCode
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_vehiculo")
    @SequenceGenerator(name = "seq_vehiculo", sequenceName = "seq_vehiculo", allocationSize = 1)
    @Column(name = "IDVEHICULO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "MARCA")
    private String marca;

    @Column(name = "MODELO")
    private String modelo;

    @Column(name = "ANIO")
    private int anio;

    @Column(name = "PLACA")
    private String placa;

    @Column(name = "VIN")
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private ClienteEntity idCliente;

    @OneToOne
    @JoinColumn(name = "IDESTADO", nullable = false)
    private EstadoEntity idEstado;
}
