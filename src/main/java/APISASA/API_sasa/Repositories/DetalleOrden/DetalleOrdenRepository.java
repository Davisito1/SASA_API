package APISASA.API_sasa.Repositories.DetalleOrden;

import APISASA.API_sasa.Entities.DetalleOrden.DetalleOrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrdenEntity, Long> {
    // - findAll()
    // - findById()
    // - save()
    // - deleteById()
    // - existsById()
}
