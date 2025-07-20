package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.MantenimientoEntity;
import APISASA.API_sasa.Models.DTO.MantenimientoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MantenimientoRepository extends JpaRepository<MantenimientoEntity, Long> {
}
