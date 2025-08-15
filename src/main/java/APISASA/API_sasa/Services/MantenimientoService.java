package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.MantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionMantenimientoNoEncontrado;
import APISASA.API_sasa.Models.DTO.MantenimientoDTO;
import APISASA.API_sasa.Repositories.MantenimientoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

// ⬇️ imports para paginación
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MantenimientoService {

    @Autowired
    private MantenimientoRepository repo;

    // =========================
    // LISTAR (todos)
    // =========================
    public List<MantenimientoDTO> obtenerMantenimientos() {
        List<MantenimientoEntity> datos = repo.findAll();
        return datos.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    // =========================
    // LISTAR (paginado + búsqueda)
    // q vacío -> todo paginado
    // q numérico -> intenta ID exacto; si no, busca por idVehiculo
    // q texto -> busca por descripcion O codigoMantenimiento (contains, ignore case)
    // =========================
    public Page<MantenimientoDTO> obtenerMantenimientosPaginado(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(this::convertirADTO);
        }

        String term = q.trim();

        // ¿número? intenta ID o idVehiculo
        try {
            Long id = Long.parseLong(term);

            // 1) ID exacto
            var opt = repo.findById(id);
            if (opt.isPresent()) {
                return new PageImpl<>(
                        List.of(convertirADTO(opt.get())),
                        pageable,
                        1
                );
            }

            // 2) por vehículo (si tienes el campo simple idVehiculo en el entity)
            return repo.findByIdVehiculo(id, pageable).map(this::convertirADTO);

        } catch (NumberFormatException ignore) {
            // texto -> buscar por descripcion o codigo
            return repo.findByDescripcionContainingIgnoreCaseOrCodigoMantenimientoContainingIgnoreCase(
                    term, term, pageable
            ).map(this::convertirADTO);
        }
    }

    // =========================
    // INSERTAR
    // =========================
    public MantenimientoDTO insertarMantenimiento(MantenimientoDTO data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos del mantenimiento no pueden ser nulos");
        }

        try {
            MantenimientoEntity entity = convertirAEntity(data);
            MantenimientoEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar mantenimiento: {}", e.getMessage());
            throw new RuntimeException("No se pudo registrar el mantenimiento.");
        }
    }

    // =========================
    // ACTUALIZAR
    // =========================
    public MantenimientoDTO actualizarMantenimiento(Long id, @Valid MantenimientoDTO data) {
        MantenimientoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionMantenimientoNoEncontrado("No se encontró mantenimiento con ID: " + id));

        existente.setDescripcion(data.getDescripcion());
        existente.setFechaRealizacion(data.getFechaRealizacion());
        existente.setCodigoMantenimiento(data.getCodigoMantenimiento());
        existente.setIdVehiculo(data.getIdVehiculo());

        MantenimientoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // =========================
    // ELIMINAR
    // =========================
    public boolean eliminarMantenimiento(Long id) {
        try {
            MantenimientoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionMantenimientoNoEncontrado("No se encontró mantenimiento con ID: " + id + " para eliminar.");
        }
    }

    // =========================
    // MAPEOS
    // =========================
    private MantenimientoEntity convertirAEntity(MantenimientoDTO dto) {
        MantenimientoEntity entity = new MantenimientoEntity();
        entity.setId(dto.getId());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaRealizacion(dto.getFechaRealizacion());
        entity.setCodigoMantenimiento(dto.getCodigoMantenimiento());
        entity.setIdVehiculo(dto.getIdVehiculo());
        return entity;
    }

    private MantenimientoDTO convertirADTO(MantenimientoEntity entity) {
        MantenimientoDTO dto = new MantenimientoDTO();
        dto.setId(entity.getId());
        dto.setDescripcion(entity.getDescripcion());
        dto.setFechaRealizacion(entity.getFechaRealizacion());
        dto.setCodigoMantenimiento(entity.getCodigoMantenimiento());
        dto.setIdVehiculo(entity.getIdVehiculo());
        return dto;
    }
}
