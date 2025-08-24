package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.CitaEntity;
import APISASA.API_sasa.Entities.ClienteEntity;
import APISASA.API_sasa.Exceptions.ExceptionCitaNoEncontrada;
import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Repositories.CitaRepository;
import APISASA.API_sasa.Repositories.ClientRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CitaService {

    @Autowired
    private CitaRepository repo;

    @Autowired
    private ClientRepository repoClient;

    // ✅ Listar todas (no paginado)
    public List<CitaDTO> obtenerCitas() {
        return repo.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Listar paginado
    public Page<CitaDTO> obtenerCitas(Pageable pageable) {
        return repo.findAll(pageable).map(this::convertirADTO);
    }

    // ✅ Obtener por ID
    public CitaDTO obtenerCitaPorId(Long id) {
        return repo.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id));
    }

    // ✅ Crear
    public CitaDTO insertarCita(@Valid CitaDTO dto) {
        try {
            CitaEntity entity = convertirAEntity(dto);
            entity.setId(null); // dejar que la secuencia maneje el ID
            CitaEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar cita: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo registrar la cita.");
        }
    }

    // ✅ Actualizar
    public CitaDTO actualizarCita(Long id, @Valid CitaDTO dto) {
        CitaEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id));

        existente.setFecha(dto.getFecha());
        existente.setHora(dto.getHora());
        existente.setEstado(dto.getEstado());

        if (dto.getIdCliente() != null) {
            ClienteEntity cliente = repoClient.getReferenceById(dto.getIdCliente());
            existente.setCliente(cliente);
        }

        CitaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar
    public boolean eliminarCita(Long id) {
        try {
            repo.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id + " para eliminar.");
        }
    }

    // ==========================
    // 🔹 Mappers
    // ==========================
    private CitaEntity convertirAEntity(CitaDTO dto) {
        CitaEntity entity = new CitaEntity();
        // ⚠️ Para insertar no hace falta asignar el ID
        entity.setId(dto.getId());
        entity.setFecha(dto.getFecha());
        entity.setHora(dto.getHora());
        entity.setEstado(dto.getEstado());

        if (dto.getIdCliente() != null) {
            ClienteEntity cliente = repoClient.getReferenceById(dto.getIdCliente());
            entity.setCliente(cliente);
        }

        return entity;
    }

    private CitaDTO convertirADTO(CitaEntity entity) {
        CitaDTO dto = new CitaDTO();
        dto.setId(entity.getId());
        dto.setFecha(entity.getFecha());
        dto.setHora(entity.getHora());
        dto.setEstado(entity.getEstado());

        if (entity.getCliente() != null) {
            dto.setIdCliente(entity.getCliente().getId());
        }

        return dto;
    }
}
