package APISASA.API_sasa.Repositories.Usuario;

import APISASA.API_sasa.Entities.Usuario.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Buscar usuario por correo
    Optional<UserEntity> findByCorreo(String correo);

    // Buscar usuario por nombre de usuario
    Optional<UserEntity> findByNombreUsuario(String nombreUsuario);

    // Validar existencia (para evitar duplicados en registro)
    boolean existsByCorreo(String correo);

    boolean existsByNombreUsuario(String nombreUsuario);
}
