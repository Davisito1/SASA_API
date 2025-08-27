package APISASA.API_sasa.Repositories.Notificaciones;

import APISASA.API_sasa.Entities.Notificaciones.NotificacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepository extends JpaRepository<NotificacionesEntity, Long> {

    // Buscar notificaciones por ID de usuario (usando el nombre real de la PK en UserEntity)
    List<NotificacionesEntity> findByUsuario_IdUsuario(Long idUsuario);
}
