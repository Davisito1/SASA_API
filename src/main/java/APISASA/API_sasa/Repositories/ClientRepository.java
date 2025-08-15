package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.ClienteEntity;
import APISASA.API_sasa.Entities.EmpleadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClienteEntity, Long> {
    Page<ClienteEntity> findAll(Pageable pageable);
}
