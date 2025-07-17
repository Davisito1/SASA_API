package APISASA.API_sasa.Entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Usuario")
@Getter @Setter  @ToString @EqualsAndHashCode
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", allocationSize = 1)
    @Column(name = "IDUSUARIO", insertable= false, updatable = false)
    private long id;
    @Column(name = "NOMBREUSUARIO")
    private String nombreUsuario;
    @Column(name = "CONTRASENA")
    private String contrasena;
    @Column(name = "ROL")
    private String rol;
    @Column(name = "ESTADO")
    private String estado;
}
