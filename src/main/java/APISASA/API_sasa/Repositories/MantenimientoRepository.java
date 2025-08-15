package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.MantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoEntity, Long> {

    // Búsqueda por descripción o código (contains, case-insensitive) + paginación
    Page<MantenimientoEntity> findByDescripcionContainingIgnoreCaseOrCodigoMantenimientoContainingIgnoreCase(
            String descripcion,
            String codigoMantenimiento,
            Pageable pageable
    );

    // Búsqueda por idVehiculo (si lo tienes como campo simple en la entidad) + paginación
    Page<MantenimientoEntity> findByIdVehiculo(Long idVehiculo, Pageable pageable);

    // (Opcional) Helper de unicidad si lo necesitas
    boolean existsByCodigoMantenimiento(String codigoMantenimiento);
}
