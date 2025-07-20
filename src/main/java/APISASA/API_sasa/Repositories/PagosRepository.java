package APISASA.API_sasa.Repositories;
import APISASA.API_sasa.Entities.PagosEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagosRepository extends JpaRepository<PagosEntity, Long> {
}
