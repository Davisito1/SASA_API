package APISASA.API_sasa.Repositories.Notificaciones;

import APISASA.API_sasa.Entities.Notificaciones.NotificacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepository extends JpaRepository<NotificacionesEntity, Long> {

    //  Obtener todas las notificaciones de un usuario
    List<NotificacionesEntity> findByUsuario_IdUsuario(Long idUsuario);

    //  Opcional: obtener solo las no leídas
    List<NotificacionesEntity> findByUsuario_IdUsuarioAndLectura(Long idUsuario, Integer lectura);
}
