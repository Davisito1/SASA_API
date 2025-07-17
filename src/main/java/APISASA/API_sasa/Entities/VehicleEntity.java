package APISASA.API_sasa.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Vehiculo")
@Getter @Setter @ToString @EqualsAndHashCode
public class VehicleEntity {

    @Id
    @Column(name = "IDVEHICULO")
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

    @Column(name = "IDCLIENTE")
    private int idCliente;

    @Column(name = "IDESTADO")
    private int idEstado;
}
