package APISASA.API_sasa.Models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class EstadoDTO {
    private long id;
    private String nombreEstado;
}
