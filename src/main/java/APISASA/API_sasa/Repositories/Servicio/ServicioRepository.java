package APISASA.API_sasa.Repositories.Servicio;

import APISASA.API_sasa.Entities.Servicio.ServicioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicioRepository extends JpaRepository<ServicioEntity, Long> {
}
