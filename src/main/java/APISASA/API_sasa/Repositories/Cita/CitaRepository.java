package APISASA.API_sasa.Repositories.Cita;

import APISASA.API_sasa.Entities.Cita.CitaEntity;
import APISASA.API_sasa.Entities.Cliente.ClienteEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<CitaEntity, Long> {

    // 🔹 Buscar citas por cliente (usando el objeto cliente)
    Page<CitaEntity> findByCliente(ClienteEntity cliente, Pageable pageable);

    // 🔹 Si solo querés buscar por el ID del cliente:
    Page<CitaEntity> findByCliente_Id(Long idCliente, Pageable pageable);
}
