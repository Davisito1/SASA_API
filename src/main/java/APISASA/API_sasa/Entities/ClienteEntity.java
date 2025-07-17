package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente")
    @SequenceGenerator(name = "seq_cliente", sequenceName = "seq_cliente", allocationSize = 1)
    @Column(name = "IDCLIENTE", insertable = false, updatable = false)
    private long id;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "APELLIDO")
    private String apellido;
    @Column(name = "DUI")
    private String dui;
    @Column(name = "FECHANACIMIENTO")
    private LocalDate fechaNacimiento;
    @Column(name = "GENERO")
    private String genero;
}
