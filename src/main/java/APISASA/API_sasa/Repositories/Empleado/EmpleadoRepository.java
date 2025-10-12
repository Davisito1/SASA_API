package APISASA.API_sasa.Repositories.Empleado;

import APISASA.API_sasa.Entities.Empleado.EmpleadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    // Búsqueda general para listados o combos
    Page<EmpleadoEntity> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDuiContainingIgnoreCaseOrCorreoElectronicoContainingIgnoreCase(
            String nombres,
            String apellidos,
            String dui,
            String correoElectronico,
            Pageable pageable
    );

    // 👇 Nuevo método para recuperar por correo
    Optional<EmpleadoEntity> findByCorreoElectronico(String correoElectronico);
}
