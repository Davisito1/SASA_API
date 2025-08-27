package APISASA.API_sasa.Entities.Vehiculo;

import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import APISASA.API_sasa.Entities.Estado.EstadoVehiculoEntity;
import APISASA.API_sasa.Entities.Historial.HistorialEntity;
import APISASA.API_sasa.Entities.Mantenimiento.MantenimientoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "VEHICULO")
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class VehicleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehiculo_seq")
    @SequenceGenerator(
            name = "vehiculo_seq",
            sequenceName = "SEQ_VEHICULO", // 👈 Debe coincidir con la secuencia en Oracle
            allocationSize = 1
    )
    @Column(name = "IDVEHICULO", insertable = false, updatable = false)
    private Long idVehiculo;

    @Column(name = "MARCA", nullable = false, length = 50)
    private String marca;

    @Column(name = "MODELO", nullable = false, length = 50)
    private String modelo;

    @Column(name = "ANIO", nullable = false)
    private int anio;

    @Column(name = "PLACA", nullable = false, unique = true, length = 20)
    private String placa;

    @Column(name = "VIN", nullable = false, unique = true, length = 17)
    private String vin;

    // 🔹 Relación con Cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDCLIENTE", nullable = false)
    private ClienteEntity cliente;

    // 🔹 Relación con EstadoVehiculo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDESTADO", nullable = false)
    private EstadoVehiculoEntity estado;

    // 🔹 Relación con Mantenimiento
    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MantenimientoEntity> mantenimientos;

    // 🔹 Relación con HistorialVehiculo
    @OneToMany(mappedBy = "vehiculo", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<HistorialEntity> historiales;
}
