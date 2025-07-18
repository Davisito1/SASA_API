package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository  extends JpaRepository<VehicleEntity, Long> {
}
