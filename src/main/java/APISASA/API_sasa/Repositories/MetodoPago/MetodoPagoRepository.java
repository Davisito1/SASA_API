package APISASA.API_sasa.Repositories.MetodoPago;
import APISASA.API_sasa.Entities.MetodoPago.MetodoPagoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPagoEntity, Long> {
    Page<MetodoPagoEntity> findAll(Pageable pageable);
    boolean existsByMetodoIgnoreCase(String metodo);
}
