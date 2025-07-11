package APISASA.API_sasa.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "CLIENTE")
@ToString
@EqualsAndHashCode
@Getter @Setter
public class ClienteEntity {
    @Id
    @Column(name = "IDCLIENTE", insertable = false, updatable = false)
    private long id;
    private String nombre;
    private String apellido;
    private String dui;
    private LocalDate fechaNacimiento;
    private String genero;
}
