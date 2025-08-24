package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.HistorialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {

    // Buscar historial por vehículo (usando la relación vehiculo en la Entity)
    List<HistorialEntity> findByVehiculo_IdVehiculo(Long idVehiculo);
}

