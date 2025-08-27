package APISASA.API_sasa.Repositories.Estado;

import APISASA.API_sasa.Entities.Estado.EstadoVehiculoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoVehiculoEntity, Long> { }
