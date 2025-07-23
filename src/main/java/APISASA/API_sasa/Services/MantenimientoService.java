package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.CitaEntity;
import APISASA.API_sasa.Entities.MantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionCitaNoEncontrada;
import APISASA.API_sasa.Exceptions.ExceptionMantenimientoNoEncontrado;
import APISASA.API_sasa.Models.DTO.CitaDTO;
import APISASA.API_sasa.Models.DTO.MantenimientoDTO;
import APISASA.API_sasa.Repositories.MantenimientoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MantenimientoService {
    @Autowired
    private MantenimientoRepository repo;

    public List<MantenimientoDTO> obtenerMantenimientos() {
        List<MantenimientoEntity> datos = repo.findAll();
        return datos.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public MantenimientoDTO insertarMantenimiento(MantenimientoDTO data) {
        if (data == null) {
            throw new IllegalArgumentException("Los datos del mantenimiento no pueden ser nulos");
        }

        try {
            MantenimientoEntity entity = convertirAEntity(data);
            MantenimientoEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar mantenimiento: " + e.getMessage());
            throw new RuntimeException("No se pudo registrar el mantenimiento.");
        }
    }

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
            throw new RuntimeException("No se encontró mantenimiento con ID: " + id + " para eliminar.");
        }
    }

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
