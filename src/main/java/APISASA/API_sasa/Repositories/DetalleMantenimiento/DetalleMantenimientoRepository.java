package APISASA.API_sasa.Repositories.DetalleMantenimiento;

import APISASA.API_sasa.Entities.DetalleMantenimiento.DetalleMantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleMantenimientoRepository extends JpaRepository<DetalleMantenimientoEntity, Long> {

    //Consultar todos los detalles de un mantenimiento específico
    List<DetalleMantenimientoEntity> findByMantenimiento_Id(Long idMantenimiento);

    //Paginado por estado (LIKE, insensible a mayúsculas)
    Page<DetalleMantenimientoEntity> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);

    //Paginado por coincidencia exacta de cualquiera de los 3 IDs
    Page<DetalleMantenimientoEntity> findByMantenimiento_IdOrServicio_IdServicioOrTipoMantenimiento_IdTipoMantenimiento(
            Long idMantenimiento,
            Long idServicio,
            Long idTipoMantenimiento,
            Pageable pageable
    );

    //Helpers
    long countByEstadoIgnoreCase(String estado);
    boolean existsByMantenimiento_Id(Long idMantenimiento);
    boolean existsByServicio_IdServicio(Long idServicio);
    boolean existsByTipoMantenimiento_IdTipoMantenimiento(Long idTipoMantenimiento);
}
