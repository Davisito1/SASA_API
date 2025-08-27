package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.MantenimientoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoEntity, Long> {

    Page<MantenimientoEntity> findByVehiculo_IdVehiculo(Long idVehiculo, Pageable pageable);

    Page<MantenimientoEntity> findByDescripcionTrabajoContainingIgnoreCaseOrCodigoMantenimientoContainingIgnoreCase(
            String descripcion, String codigo, Pageable pageable
    );

    @Query("SELECT MAX(CAST(SUBSTRING(m.codigoMantenimiento, 10, 3) AS int)) " +
            "FROM MantenimientoEntity m " +
            "WHERE SUBSTRING(m.codigoMantenimiento, 5, 4) = :year")
    Integer findUltimoCorrelativo(@Param("year") int year);
}
