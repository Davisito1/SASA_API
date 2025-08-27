package APISASA.API_sasa.Repositories.TipoMantenimiento;
import APISASA.API_sasa.Entities.TipoMantenimiento.TipoMantenimientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoMantenimientoRepository extends JpaRepository<TipoMantenimientoEntity, Long> {
}
