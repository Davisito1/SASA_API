package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.EstadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<EstadoEntity, Long> {
    Page<EstadoEntity> findAll(Pageable pageable);
}
