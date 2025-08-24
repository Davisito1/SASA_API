package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "EMPLEADO")
@ToString @EqualsAndHashCode @Getter @Setter
public class EmpleadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_empleado")
    @SequenceGenerator(name = "seq_empleado", sequenceName = "seq_empleado", allocationSize = 1)
    @Column(name = "IDEMPLEADO", insertable = false, updatable = false)
    private Long id;

    @Column(name = "NOMBRES", nullable = false, length = 100)
    private String nombres;

    @Column(name = "APELLIDOS", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "CARGO", nullable = false)
    private String cargo;

    @Column(name = "DUI", nullable = false)
    private String dui;

    @Column(name = "TELEFONO", nullable = false)
    private String telefono;

    @Column(name = "DIRECCION", nullable = false)
    private String direccion;

    @Column(name = "FECHACONTRATACION", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "CORREOELECTRONICO", nullable = false, unique = true)
    private String correo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO", nullable = false)
    private UserEntity idUsuario;
}
