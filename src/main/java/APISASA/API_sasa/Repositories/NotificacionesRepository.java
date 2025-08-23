package APISASA.API_sasa.Repositories;
import APISASA.API_sasa.Entities.NotificacionesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepository extends JpaRepository<NotificacionesEntity, Long> {
    List<NotificacionesEntity> findByIdUsuario(Long idUsuario);
}
