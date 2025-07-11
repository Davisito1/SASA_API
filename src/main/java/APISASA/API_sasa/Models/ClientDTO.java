package APISASA.API_sasa.Models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class ClientDTO {
    private long id;
    private String nombre;
    private String apellido;
    private String dui;
    private LocalDate fechaNacimiento;
    private String genero;
}
