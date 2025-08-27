package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EmpleadoEntity;
import APISASA.API_sasa.Entities.UserEntity;
import APISASA.API_sasa.Exceptions.ExceptionEmpleadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.EmpleadoDTO;
import APISASA.API_sasa.Repositories.EmpleadoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repo;

    @PersistenceContext
    private EntityManager em;

    // ✅ Listar todos
    public List<EmpleadoDTO> obtenerEmpleados() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Listar con paginación + búsqueda
    public Page<EmpleadoDTO> obtenerEmpleadosPaginado(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(this::convertirADTO);
        }
        String term = q.trim();
        return repo.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCaseOrDuiContainingIgnoreCaseOrCorreoElectronicoContainingIgnoreCase(
                        term, term, term, term, pageable
                )
                .map(this::convertirADTO);
    }

    // ✅ Paginación simple (sin búsqueda extra)
    public Page<EmpleadoDTO> obtenerEmpleados(Pageable pageable) {
        return repo.findAll(pageable).map(this::convertirADTO);
    }

    // ✅ Insertar
    public EmpleadoDTO insertarEmpleado(EmpleadoDTO dto) {
        EmpleadoEntity entity = convertirAEntity(dto);
        EmpleadoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // ✅ Actualizar
    public EmpleadoDTO actualizarEmpleado(Long id, EmpleadoDTO dto) {
        EmpleadoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionEmpleadoNoEncontrado("No existe un empleado con ID: " + id));

        existente.setNombres(dto.getNombres());
        existente.setApellidos(dto.getApellidos());
        existente.setCargo(dto.getCargo());
        existente.setDui(dto.getDui());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());
        existente.setFechaContratacion(dto.getFechaContratacion());
        existente.setCorreoElectronico(dto.getCorreo());

        if (dto.getIdUsuario() != null) {
            existente.setUsuario(em.getReference(UserEntity.class, dto.getIdUsuario()));
        }

        return convertirADTO(repo.save(existente));
    }

    // ✅ Eliminar
    public boolean eliminarEmpleado(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionEmpleadoNoEncontrado("No se encontró el empleado con ID: " + id + " para eliminar.");
        }
    }

    // ================== Conversores ==================
    private EmpleadoDTO convertirADTO(EmpleadoEntity entity) {
        EmpleadoDTO dto = new EmpleadoDTO();
        dto.setId(entity.getIdEmpleado());
        dto.setNombres(entity.getNombres());
        dto.setApellidos(entity.getApellidos());
        dto.setCargo(entity.getCargo());
        dto.setDui(entity.getDui());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        dto.setFechaContratacion(entity.getFechaContratacion());
        dto.setCorreo(entity.getCorreoElectronico());

        if (entity.getUsuario() != null) {
            dto.setIdUsuario(entity.getUsuario().getIdUsuario());
        }
        return dto;
    }

    private EmpleadoEntity convertirAEntity(EmpleadoDTO dto) {
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setNombres(dto.getNombres());
        entity.setApellidos(dto.getApellidos());
        entity.setCargo(dto.getCargo());
        entity.setDui(dto.getDui());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        entity.setFechaContratacion(dto.getFechaContratacion());
        entity.setCorreoElectronico(dto.getCorreo());

        if (dto.getIdUsuario() != null) {
            entity.setUsuario(em.getReference(UserEntity.class, dto.getIdUsuario()));
        }
        return entity;
    }
}
