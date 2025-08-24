package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClienteEntity, Long> {

    // 🔹 Verificar si existe un cliente con un DUI específico (útil para validaciones)
    boolean existsByDui(String dui);
}
