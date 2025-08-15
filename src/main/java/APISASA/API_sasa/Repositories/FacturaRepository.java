package APISASA.API_sasa.Repositories;
import APISASA.API_sasa.Entities.FacturaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<FacturaEntity, Long> {
    Page<FacturaEntity> findAll(Pageable pageable);
}
