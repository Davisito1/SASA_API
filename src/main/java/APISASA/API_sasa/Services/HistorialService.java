package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.FacturaEntity;
import APISASA.API_sasa.Entities.HistorialEntity;
import APISASA.API_sasa.Exceptions.ExceptionHistorialNoEncontrado;
import APISASA.API_sasa.Models.DTO.HistorialDTO;
import APISASA.API_sasa.Repositories.HistorialRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HistorialService {

    @Autowired
    private HistorialRepository repo;

    public Page<HistorialDTO> obtenerHistorial(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistorialEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirADTO);
    }

    public HistorialDTO insertarHistorial(HistorialDTO dto) {
        try {
            HistorialEntity entity = convertirAEntity(dto);
            HistorialEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar historial: {}", e.getMessage());
            throw new RuntimeException("No se pudo registrar el historial.");
        }
    }

    public HistorialDTO actualizarHistorial(Long id, @Valid HistorialDTO dto) {
        HistorialEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionHistorialNoEncontrado("No se encontró historial con ID: " + id));

        existente.setFechaIngreso(dto.getFechaIngreso());
        existente.setFechaSalida(dto.getFechaSalida());
        existente.setTrabajoRealizado(dto.getTrabajoRealizado());
        existente.setObservaciones(dto.getObservaciones());
        existente.setIdVehiculo(dto.getIdVehiculo());

        HistorialEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarHistorial(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró historial con ID: " + id + " para eliminar.");
        }
    }

    private HistorialEntity convertirAEntity(HistorialDTO dto) {
        HistorialEntity entity = new HistorialEntity();
        entity.setId(dto.getId());
        entity.setFechaIngreso(dto.getFechaIngreso());
        entity.setFechaSalida(dto.getFechaSalida());
        entity.setTrabajoRealizado(dto.getTrabajoRealizado());
        entity.setObservaciones(dto.getObservaciones());
        entity.setIdVehiculo(dto.getIdVehiculo());
        return entity;
    }

    private HistorialDTO convertirADTO(HistorialEntity entity) {
        HistorialDTO dto = new HistorialDTO();
        dto.setId(entity.getId());
        dto.setFechaIngreso(entity.getFechaIngreso());
        dto.setFechaSalida(entity.getFechaSalida());
        dto.setTrabajoRealizado(entity.getTrabajoRealizado());
        dto.setObservaciones(entity.getObservaciones());
        dto.setIdVehiculo(entity.getIdVehiculo());
        return dto;
    }
}
