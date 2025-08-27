package APISASA.API_sasa.Entities.Usuario;

import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import APISASA.API_sasa.Entities.Notificaciones.NotificacionesEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "IDUSUARIO")
    private Long idUsuario;

    @Column(name = "NOMBREUSUARIO", nullable = false, unique = true, length = 100)
    private String nombreUsuario;

    @Column(name = "CONTRASENA", nullable = false, length = 100)
    private String contrasena;

    @Column(name = "ROL", nullable = false, length = 50)
    private String rol;

    @Column(name = "ESTADO", length = 100)
    private String estado;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmpleadoEntity> empleados = new ArrayList<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificacionesEntity> notificaciones = new ArrayList<>();
}

