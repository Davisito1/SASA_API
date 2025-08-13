package APISASA.API_sasa.Repositories;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadoEntity, Long> {

    // Busca por nombres, apellidos, DUI o correo (case-insensitive) y devuelve paginado
    Page<EmpleadoEntity> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDuiContainingIgnoreCaseOrCorreoContainingIgnoreCase(
            String nombres, String apellidos, String dui, String correo, Pageable pageable
    );
}
