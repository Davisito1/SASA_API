package APISASA.API_sasa.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @EqualsAndHashCode
public class VehicleDTO {
    private long id;

    @NotBlank(message = "La marca no puede estar vacía")
    private String marca;

    @NotBlank(message = "El modelo no puede estar vacío")
    private String modelo;

    @Min(value = 1900, message = "El año no puede ser menor a 1900")
    @Max(value = 2100, message = "El año no puede ser mayor a 2100")
    private int anio;

    @NotBlank(message = "La placa no puede estar vacía")
    @Pattern(regexp = "^[A-Z0-9-]{5,10}$", message = "La placa debe tener entre 5 y 10 caracteres alfanuméricos")
    private String placa;

    @NotBlank(message = "El VIN no puede estar vacío")
    @Size(min = 17, max = 17, message = "El VIN debe tener exactamente 17 caracteres")
    private String vin;

    @Min(value = 1, message = "Debe asignarse un cliente válido")
    private int idCliente;

    @Min(value = 1, message = "Debe asignarse un estado válido")
    private int idEstado;
}