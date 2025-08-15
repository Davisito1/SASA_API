package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.ClienteEntity;
import APISASA.API_sasa.Entities.EstadoEntity;
import APISASA.API_sasa.Exceptions.ExceptionEstadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.EstadoDTO;
import APISASA.API_sasa.Repositories.EstadoRepository;
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
public class EstadoService {

    @Autowired
    private EstadoRepository repo;

    public Page<EstadoDTO> getAllEstados(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<EstadoEntity> pageEntity = repo.findAll(pageable);
        return pageEntity.map(this::convertirAEstadoDTO);
    }

    public EstadoDTO createEstado(EstadoDTO estadoDTO) {
        if (estadoDTO == null || estadoDTO.getNombreEstado() == null || estadoDTO.getNombreEstado().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado no puede ser nulo ni vacío.");
        }
        try {
            EstadoEntity entity = convertirAEstadoEntity(estadoDTO);
            EstadoEntity guardado = repo.save(entity);
            return convertirAEstadoDTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar estado: {}", e.getMessage());
            throw new ExceptionEstadoNoEncontrado("Error al registrar el estado: " + e.getMessage());
        }
    }

    public EstadoDTO updateEstado(Long id, EstadoDTO estadoDTO) {
        EstadoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionEstadoNoEncontrado("Estado no encontrado con ID: " + id));

        existente.setNombreEstado(estadoDTO.getNombreEstado());
        EstadoEntity actualizado = repo.save(existente);
        return convertirAEstadoDTO(actualizado);
    }

    public boolean deleteEstado(Long id) {
        try {
            EstadoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                log.warn("Estado con ID {} no encontrado para eliminar.", id);
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new EmptyResultDataAccessException("No se encontró el estado con ID: " + id + " para eliminar.", 1);
        }
    }

    // Conversores
    public EstadoEntity convertirAEstadoEntity(EstadoDTO dto) {
        EstadoEntity entity = new EstadoEntity();
        entity.setId(dto.getId()); // opcional, depende de la lógica
        entity.setNombreEstado(dto.getNombreEstado());
        return entity;
    }

    public EstadoDTO convertirAEstadoDTO(EstadoEntity entity) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(entity.getId());
        dto.setNombreEstado(entity.getNombreEstado());
        return dto;
    }
}
