package APISASA.API_sasa.Repositories.DetalleOrden;

import APISASA.API_sasa.Entities.DetalleOrden.DetalleOrdenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleOrdenRepository extends JpaRepository<DetalleOrdenEntity, Long> {
    List<DetalleOrdenEntity> findByOrdenTrabajo_IdOrden(Long idOrden);}

