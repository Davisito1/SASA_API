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
    private ClientRepository repoClient;

    // ======= EXISTENTES (los dejo intactos) =======

    // ✅ Listar todas (no paginado)
    public List<CitaDTO> obtenerCitas() {
        List<CitaEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    // ✅ Obtener por ID
    public CitaDTO obtenerCitaPorId(Long id) {
        return repo.findById(id)
                .map(this::convertirADTO)
                .orElseThrow(() -> new ExceptionCitaNoEncontrada(
                        "No se encontró cita con ID: " + id
                ));
    }

    // ✅ Crear
    public CitaDTO insertarCita(@Valid CitaDTO data) {
        if (data == null || data.getFecha() == null || data.getHora() == null) {
            throw new IllegalArgumentException("Los datos de la cita no pueden ser nulos");
        }
        try {
            CitaEntity entity = convertirAEntity(data);
            CitaEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar cita: {}", e.getMessage(), e);
            throw new RuntimeException("No se pudo registrar la cita.");
        }
    }

    // ✅ Actualizar
    public CitaDTO actualizarCita(Long id, @Valid CitaDTO data) {
        CitaEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionCitaNoEncontrada("No se encontró cita con ID: " + id));

        existente.setFecha(data.getFecha());
        existente.setHora(data.getHora());
        existente.setEstado(data.getEstado());
        if (data.getIdCliente() != null) existente.setIdCliente(repoClient.getReferenceById(data.getIdCliente()));

        CitaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar
    public boolean eliminarCita(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            }
            return false;
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró cita con ID: " + id + " para eliminar.");
        }
    }

    // ======= NUEVOS (paginado + búsqueda) =======

    // ✅ Listar paginado
    public Page<CitaDTO> obtenerCitas(Pageable pageable) {
        return repo.findAll(pageable).map(this::convertirADTO);
    }

    // ✅ Búsqueda paginada: si 'q' es número -> idCliente; si no -> estado LIKE
    public Page<CitaDTO> buscarCitas(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return obtenerCitas(pageable);
        }
        try {
            long idCliente = Long.parseLong(q.trim());
            return repo.findByIdCliente(idCliente, pageable).map(this::convertirADTO);
        } catch (NumberFormatException ignore) {
            return repo.findByEstadoContainingIgnoreCase(q.trim(), pageable).map(this::convertirADTO);
        }
    }

    // ======= Mappers =======
    private CitaEntity convertirAEntity(CitaDTO dto) {
        CitaEntity entity = new CitaEntity();
        entity.setId(dto.getId());
        entity.setFecha(dto.getFecha());
        entity.setHora(dto.getHora());
        entity.setEstado(dto.getEstado());
        if (dto.getIdCliente() != null) entity.setIdCliente(repoClient.getReferenceById(dto.getIdCliente()));
        return entity;
    }

    private CitaDTO convertirADTO(CitaEntity citaEntity) {
        CitaDTO dto = new CitaDTO();
        dto.setId(citaEntity.getId());
        dto.setFecha(citaEntity.getFecha());
        dto.setHora(citaEntity.getHora());
        dto.setEstado(citaEntity.getEstado());
        if (citaEntity.getIdCliente() != null) dto.setIdCliente(citaEntity.getIdCliente().getId());
        return dto;
    }
}
