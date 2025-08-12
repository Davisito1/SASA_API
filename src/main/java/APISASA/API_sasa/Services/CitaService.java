package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.CitaEntity;
import APISASA.API_sasa.Exceptions.ExceptionCitaNoEncontrada;
import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Repositories.CitaRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CitaService {

    @Autowired
    private CitaRepository repo;

    // ✅ Listar todas
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
    public CitaDTO insertarCita(CitaDTO data) {
        if (data == null || data.getFecha() == null || data.getHora() == null) {
            throw new IllegalArgumentException("Los datos de la cita no pueden ser nulos");
        }

        try {
            CitaEntity entity = convertirAEntity(data);
            CitaEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar cita: " + e.getMessage());
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
        existente.setIdCliente(data.getIdCliente());

        CitaEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar
    public boolean eliminarCita(Long id) {
        try {
            CitaEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró cita con ID: " + id + " para eliminar.");
        }
    }

    // 🔹 Conversión DTO -> Entity
    private CitaEntity convertirAEntity(CitaDTO dto) {
        CitaEntity entity = new CitaEntity();
        entity.setId(dto.getId());
        entity.setFecha(dto.getFecha());
        entity.setHora(dto.getHora());
        entity.setEstado(dto.getEstado());
        entity.setIdCliente(dto.getIdCliente());
        return entity;
    }

    // 🔹 Conversión Entity -> DTO
    private CitaDTO convertirADTO(CitaEntity citaEntity) {
        CitaDTO dto = new CitaDTO();
        dto.setId(citaEntity.getId());
        dto.setFecha(citaEntity.getFecha());
        dto.setHora(citaEntity.getHora());
        dto.setEstado(citaEntity.getEstado());
        dto.setIdCliente(citaEntity.getIdCliente());
        return dto;
    }
}
