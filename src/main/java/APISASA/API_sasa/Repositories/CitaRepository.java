package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.CitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {
}
