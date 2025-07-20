package APISASA.API_sasa.Repositories;
import APISASA.API_sasa.Entities.DetalleMantenimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleMantenimientoRepository extends JpaRepository<DetalleMantenimientoEntity, Long> {
}
