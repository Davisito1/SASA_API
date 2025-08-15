package APISASA.API_sasa.Repositories;
import APISASA.API_sasa.Entities.HistorialEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistorialRepository extends JpaRepository<HistorialEntity, Long> {
    Page<HistorialEntity> findAll(Pageable pageable);
}
