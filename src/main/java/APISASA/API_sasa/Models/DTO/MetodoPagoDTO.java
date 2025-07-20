package APISASA.API_sasa.Models.DTO;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter @Setter
public class MetodoPagoDTO {
    private Long id;
    private String metodo;
}
