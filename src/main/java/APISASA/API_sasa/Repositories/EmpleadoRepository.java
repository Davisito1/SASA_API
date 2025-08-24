package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    // ✅ Usamos correoElectronico (el nombre real del atributo en la entidad)
    Page<EmpleadoEntity> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDuiContainingIgnoreCaseOrCorreoElectronicoContainingIgnoreCase(
            String nombres,
            String apellidos,
            String dui,
            String correoElectronico,
            Pageable pageable
    );
}
