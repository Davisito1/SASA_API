package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.MantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoEntity, Long> {

    // 🔹 Buscar por vehículo (usando idVehiculo correcto)
    Page<MantenimientoEntity> findByVehiculo_IdVehiculo(Long idVehiculo, Pageable pageable);

    // 🔹 Buscar por descripción o código de mantenimiento (texto, case-insensitive)
    Page<MantenimientoEntity> findByDescripcionTrabajoContainingIgnoreCaseOrCodigoMantenimientoContainingIgnoreCase(
            String descripcion, String codigo, Pageable pageable
    );
}
