package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.CitaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {

    // Debe existir un atributo EXACTO "estado" en CitaEntity
    Page<CitaEntity> findByEstadoContainingIgnoreCase(String estado, Pageable pageable);

    // Debe existir un atributo EXACTO "idCliente" en CitaEntity (Long o long)
    Page<CitaEntity> findByIdCliente(Long idCliente, Pageable pageable);
}
