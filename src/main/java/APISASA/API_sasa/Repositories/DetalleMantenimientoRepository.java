package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.DetalleMantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleMantenimientoRepository extends JpaRepository<DetalleMantenimientoEntity, Long> {

    // Paginado por estado (LIKE, sin sensibilidad a mayúsculas)
    Page<DetalleMantenimientoEntity> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);

    // Paginado por coincidencia exacta de cualquiera de los 3 IDs
    Page<DetalleMantenimientoEntity> findByIdMantenimientoOrIdServicioOrIdTipoMantenimiento(
            Long idMantenimiento,
            Long idServicio,
            Long idTipoMantenimiento,
            Pageable pageable
    );

    // (Opcional) Ayudas rápidas
    long countByEstadoIgnoreCase(String estado);
    boolean existsByIdMantenimiento_Id(Long idMantenimiento);
    boolean existsByIdServicio_Id(Long idServicio);
    boolean existsByIdTipoMantenimiento_Id(Long idTipoMantenimiento);
}
