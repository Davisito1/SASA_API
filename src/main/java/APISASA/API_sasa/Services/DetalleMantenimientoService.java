package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.DetalleMantenimientoEntity;
import APISASA.API_sasa.Exceptions.ExceptionDetalleNoEncontrado;
import APISASA.API_sasa.Models.DTO.DetalleMantenimientoDTO;
import APISASA.API_sasa.Repositories.DetalleMantenimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetalleMantenimientoService {

    @Autowired
    private DetalleMantenimientoRepository repo;

    // CONSULTAR TODOS
    public List<DetalleMantenimientoDTO> obtenerDetalles() {
        List<DetalleMantenimientoEntity> lista = repo.findAll();
        return lista.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // INSERTAR NUEVO
    public DetalleMantenimientoDTO insertarDetalle(DetalleMantenimientoDTO dto) {
        DetalleMantenimientoEntity entity = convertirAEntity(dto);
        DetalleMantenimientoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // ACTUALIZAR
    public DetalleMantenimientoDTO actualizarDetalle(Long id, DetalleMantenimientoDTO dto) {
        DetalleMantenimientoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionDetalleNoEncontrado("No existe un detalle con ID: " + id));

        existente.setEstado(dto.getEstado());
        existente.setIdMantenimiento(dto.getIdMantenimiento());
        existente.setIdServicio(dto.getIdServicio());
        existente.setIdTipoMantenimiento(dto.getIdTipoMantenimiento());

        DetalleMantenimientoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ELIMINAR
    public boolean eliminarDetalle(Long id) {
        try {
            DetalleMantenimientoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionDetalleNoEncontrado("No se encontró el detalle con ID: " + id + " para eliminar.");
        }
    }

    // CONVERTIR A DTO
    private DetalleMantenimientoDTO convertirADTO(DetalleMantenimientoEntity entity) {
        DetalleMantenimientoDTO dto = new DetalleMantenimientoDTO();
        dto.setId(entity.getIdDetalleMantenimiento());
        dto.setEstado(entity.getEstado());
        dto.setIdMantenimiento(entity.getIdMantenimiento());
        dto.setIdServicio(entity.getIdServicio());
        dto.setIdTipoMantenimiento(entity.getIdTipoMantenimiento());
        return dto;
    }

    // CONVERTIR A ENTITY
    private DetalleMantenimientoEntity convertirAEntity(DetalleMantenimientoDTO dto) {
        DetalleMantenimientoEntity entity = new DetalleMantenimientoEntity();
        entity.setEstado(dto.getEstado());
        entity.setIdMantenimiento(dto.getIdMantenimiento());
        entity.setIdServicio(dto.getIdServicio());
        entity.setIdTipoMantenimiento(dto.getIdTipoMantenimiento());
        return entity;
    }
}

