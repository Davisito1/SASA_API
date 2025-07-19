package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "CLIENTE")
@Getter @Setter @ToString @EqualsAndHashCode
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cliente")
    @SequenceGenerator(name = "seq_cliente", sequenceName = "seq_cliente", allocationSize = 1)
    @Column(name = "IDCLIENTE", insertable = false, updatable = false)
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 100)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false, length = 100)
    private String apellido;

    @Column(name = "DUI", nullable = false, unique = true, length = 10)
    private String dui;

    @Column(name = "FECHANACIMIENTO", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "GENERO", length = 20)
    private String genero;
}
