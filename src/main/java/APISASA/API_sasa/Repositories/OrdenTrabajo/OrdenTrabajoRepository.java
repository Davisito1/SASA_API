package APISASA.API_sasa.Repositories.OrdenTrabajo;

import APISASA.API_sasa.Entities.OrdenTrabajo.OrdenTrabajoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajoEntity, Long> {
    // 🔹 Podés agregar queries personalizadas si necesitas,
    // ejemplo: List<OrdenTrabajoEntity> findByVehiculo_IdVehiculo(Long idVehiculo);
}
