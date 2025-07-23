package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.MetodoPagoEntity;
import APISASA.API_sasa.Entities.TipoMantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionMetodoNoEncontrado;
import APISASA.API_sasa.Exceptions.ExceptionTipoNoEncontrado;
import APISASA.API_sasa.Models.DTO.MetodoPagoDTO;
import APISASA.API_sasa.Models.DTO.TipoMantenimientoDTO;
import APISASA.API_sasa.Repositories.TipoMantenimientoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TipoMantenimientoService {
    @Autowired
    private TipoMantenimientoRepository repo;

    public List<TipoMantenimientoDTO> obtenerTipos() {
        List<TipoMantenimientoEntity> datos = repo.findAll();
        return datos.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public TipoMantenimientoDTO insertarTipo(TipoMantenimientoDTO data) {
        if (data == null || data.getTipoMantenimiento() == null) {
            throw new IllegalArgumentException("Los datos del tipo de mantenimiento no pueden ser nulos");
        }

        try {
            TipoMantenimientoEntity entity = convertirAEntity(data);
            TipoMantenimientoEntity guardado = repo.save(entity);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar tipo de mantenimiento: " + e.getMessage());
            throw new RuntimeException("No se pudo registrar el tipo.");
        }
    }

    public TipoMantenimientoDTO actualizarTipo(Long id, @Valid TipoMantenimientoDTO data) {
        TipoMantenimientoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionTipoNoEncontrado("No se encontró tipo de mantenimiento con ID: " + id));

        existente.setTipoMantenimiento(data.getTipoMantenimiento());

        TipoMantenimientoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarTipo(Long id) {
        try {
            TipoMantenimientoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("No se encontró tipo de mantenimiento con ID: " + id + " para eliminar.");
        }
    }

    private TipoMantenimientoDTO convertirADTO(TipoMantenimientoEntity entity) {
        TipoMantenimientoDTO dto = new TipoMantenimientoDTO();
        dto.setId(entity.getId());
        dto.setTipoMantenimiento(entity.getTipoMantenimiento());
        return dto;
    }

    private TipoMantenimientoEntity convertirAEntity(TipoMantenimientoDTO dto) {
        TipoMantenimientoEntity entity = new TipoMantenimientoEntity();
        entity.setId(dto.getId());
        entity.setTipoMantenimiento(dto.getTipoMantenimiento());
        return entity;
    }
}
