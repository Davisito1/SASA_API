package APISASA.API_sasa.Entities.Empleado;

import APISASA.API_sasa.Entities.Factura.FacturaEntity;
import APISASA.API_sasa.Entities.Usuario.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EMPLEADO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empleado_seq")
    @SequenceGenerator(name = "empleado_seq", sequenceName = "SEQ_EMPLEADO", allocationSize = 1)
    @Column(name = "IDEMPLEADO")
    private Long idEmpleado;

    @Column(name = "NOMBRES", nullable = false, length = 100)
    private String nombres;

    @Column(name = "APELLIDOS", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "CARGO", nullable = false, length = 100)
    private String cargo;

    @Column(name = "DUI", unique = true, length = 10)
    private String dui;

    @Column(name = "TELEFONO", nullable = false, length = 20)
    private String telefono;

    @Column(name = "DIRECCION", nullable = false, length = 100)
    private String direccion;

    @Column(name = "FECHACONTRATACION", nullable = false)
    private LocalDate fechaContratacion;

    @Column(name = "FECHANACIMIENTO",nullable = false)
    private  LocalDate fechaNacimiento;

    @Column(name = "CORREOELECTRONICO", length = 100)
    private String correoElectronico;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IDUSUARIO", nullable = false)
    private UserEntity usuario;

    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaEntity> facturas = new ArrayList<>();
}
