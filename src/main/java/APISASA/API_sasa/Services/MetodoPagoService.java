package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.CitaEntity;
import APISASA.API_sasa.Entities.MetodoPagoEntity;
import APISASA.API_sasa.Exceptions.ExceptionCitaNoEncontrada;
import APISASA.API_sasa.Exceptions.ExceptionMetodoNoEncontrado;
import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Models.DTO.MetodoPagoDTO;
import APISASA.API_sasa.Repositories.MetodoPagoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MetodoPagoService {
    @Autowired
    private MetodoPagoRepository repo;

    public List<MetodoPagoDTO> obtenerMetodosDePago() {
        List<MetodoPagoEntity> datos = repo.findAll();
        return datos.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public MetodoPagoDTO insertarMetodo(MetodoPagoDTO data) {
        if (data == null || data.getMetodo() == null) {
            throw new IllegalArgumentException("Los datos del método no pueden ser nulos");
        }

        try {
            MetodoPagoEntity entity = convertirAEntity(data);
            MetodoPagoEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar método: " + e.getMessage());
            throw new RuntimeException("No se pudo registrar el método.");
        }
    }

    public MetodoPagoDTO actualizarMetodo(Long id, @Valid MetodoPagoDTO data) {
        MetodoPagoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionMetodoNoEncontrado("No se encontró método con ID: " + id));

        existente.setMetodo(data.getMetodo());

        MetodoPagoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarMetodo(Long id) {
        try {
            MetodoPagoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró método con ID: " + id + " para eliminar.");
        }
    }

    private MetodoPagoDTO convertirADTO(MetodoPagoEntity metodoPagoEntity) {
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setId(metodoPagoEntity.getId());
        dto.setMetodo(metodoPagoEntity.getMetodo());
        return dto;
    }

    private MetodoPagoEntity convertirAEntity(MetodoPagoDTO dto) {
        MetodoPagoEntity entity = new MetodoPagoEntity();
        entity.setId(dto.getId());
        entity.setMetodo(dto.getMetodo());
        return entity;
    }
}
