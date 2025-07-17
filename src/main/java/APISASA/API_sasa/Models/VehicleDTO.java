package APISASA.API_sasa.Models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class VehicleDTO {
    private long id;
    private String marca;
    private String modelo;
    private int anio;
    private String placa;
    private String vin;
    private ClientDTO IdCliente;
    private int idEstado;
}
