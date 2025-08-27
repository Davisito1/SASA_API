package APISASA.API_sasa.Services.Servicio;

import APISASA.API_sasa.Entities.Servicio.ServicioEntity;
import APISASA.API_sasa.Exceptions.ExceptionServicioNoEncontrado;
import APISASA.API_sasa.Models.DTO.Servicio.ServicioDTO;
import APISASA.API_sasa.Repositories.Servicio.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository repo;

    // ✅ Consultar todos
    public List<ServicioDTO> obtenerServicios() {
        return repo.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // ✅ Insertar
    public ServicioDTO insertarServicio(ServicioDTO dto) {
        ServicioEntity entity = convertirAEntity(dto);
        entity.setIdServicio(null); // que Oracle maneje el ID
        ServicioEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    // ✅ Actualizar
    public ServicioDTO actualizarServicio(Long id, ServicioDTO dto) {
        ServicioEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionServicioNoEncontrado("No existe un servicio con ID: " + id));

        existente.setNombreServicio(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());
        existente.setPrecio(dto.getPrecio());
        existente.setDuracionEstimada(dto.getDuracion());

        ServicioEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar
    public boolean eliminarServicio(Long id) {
        try {
            if (repo.existsById(id)) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionServicioNoEncontrado("No se encontró el servicio con ID: " + id + " para eliminar.");
        }
    }

    // =======================
    // 🔹 Mappers
    // =======================
    private ServicioDTO convertirADTO(ServicioEntity entity) {
        ServicioDTO dto = new ServicioDTO();
        dto.setId(entity.getIdServicio());  // ⚠️ si en tu entity es idServicio, cámbialo a getIdServicio()
        dto.setNombre(entity.getNombreServicio());
        dto.setDescripcion(entity.getDescripcion());
        dto.setPrecio(entity.getPrecio());
        dto.setDuracion(entity.getDuracionEstimada()); // ⚠️ si tu entity lo llamaste duracion
        return dto;
    }

    private ServicioEntity convertirAEntity(ServicioDTO dto) {
        ServicioEntity entity = new ServicioEntity();
        entity.setNombreServicio(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setPrecio(dto.getPrecio());
        entity.setDuracionEstimada(dto.getDuracion()); // ⚠️ si tu entity lo llamaste duracion
        return entity;
    }
}
