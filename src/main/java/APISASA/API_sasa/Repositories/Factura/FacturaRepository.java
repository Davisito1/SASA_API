package APISASA.API_sasa.Repositories.Factura;

import APISASA.API_sasa.Entities.Factura.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<FacturaEntity, Long> {
    // 🔹 Borrar todas las facturas asociadas a una orden
    void deleteAllByOrdenTrabajo_IdOrden(Long idOrden);

    // (opcional) si querés consultarlas también
    List<FacturaEntity> findByOrdenTrabajo_IdOrden(Long idOrden);
}
