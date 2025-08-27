package APISASA.API_sasa.Repositories.Factura;

import APISASA.API_sasa.Entities.Factura.FacturaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaRepository extends JpaRepository<FacturaEntity, Long> {

}
