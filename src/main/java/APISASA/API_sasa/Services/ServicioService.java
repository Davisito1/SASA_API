package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.ServicioEntity;
import APISASA.API_sasa.Exceptions.ExceptionServicioNoEncontrado;
import APISASA.API_sasa.Models.DTO.ServicioDTO;
import APISASA.API_sasa.Repositories.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository repo;

    public List<ServicioDTO> obtenerServicios() {
        List<ServicioEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public ServicioDTO insertarServicio(ServicioDTO dto) {
        ServicioEntity entity = convertirAEntity(dto);
        ServicioEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    public ServicioDTO actualizarServicio(Long id, ServicioDTO dto) {
        ServicioEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionServicioNoEncontrado("No existe un servicio con ID: " + id));

        existente.setNombreServicio(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setDuracion(dto.getDuracion());

        ServicioEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarServicio(Long id) {
        try {
            ServicioEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServicioNoEncontrado("No se encontró el servicio con ID: " + id + " para eliminar.");
        }
    }

    private ServicioDTO convertirADTO(ServicioEntity entity) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombreServicio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecio(entity.getPrecio());
        dto.setDuracion(entity.getDuracion());
        return dto;
    }

    private ServicioEntity convertirAEntity(ServicioDTO dto) {
        ServicioEntity entity = new ServicioEntity();
        entity.setNombreServicio(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPrecio(dto.getPrecio());
        entity.setDuracion(dto.getDuracion());
        return entity;
    }
}
