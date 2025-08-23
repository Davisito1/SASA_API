package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.HistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {

    // ✅ Consultar historial de un vehículo
    List<HistorialEntity> findByIdVehiculo(Long idVehiculo);

}
