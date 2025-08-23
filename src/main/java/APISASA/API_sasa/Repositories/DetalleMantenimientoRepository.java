package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.DetalleMantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleMantenimientoRepository extends JpaRepository<DetalleMantenimientoEntity, Long> {

    // ✅ Consultar todos los detalles de un mantenimiento específico
    List<DetalleMantenimientoEntity> findByIdMantenimiento(Long idMantenimiento);

    // 🔹 Paginado por estado (LIKE, sin sensibilidad a mayúsculas)
    Page<DetalleMantenimientoEntity> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);

    // 🔹 Paginado por coincidencia exacta de cualquiera de los 3 IDs
    Page<DetalleMantenimientoEntity> findByIdMantenimientoOrIdServicioOrIdTipoMantenimiento(
            Long idMantenimiento,
            Long idServicio,
            Long idTipoMantenimiento,
            Pageable pageable
    );

    // (Opcional) Ayudas rápidas
    long countByEstadoIgnoreCase(String estado);
    boolean existsByIdMantenimiento(Long idMantenimiento);
    boolean existsByIdServicio(Long idServicio);
    boolean existsByIdTipoMantenimiento(Long idTipoMantenimiento);
}
