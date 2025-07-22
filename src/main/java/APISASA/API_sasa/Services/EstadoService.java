package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.EstadoEntity;
import APISASA.API_sasa.Exceptions.ExceptionEstadoNoEncontrado;
import APISASA.API_sasa.Models.DTO.EstadoDTO;
import APISASA.API_sasa.Repositories.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoService {

    @Autowired
    private EstadoRepository repo;

    public List<EstadoDTO> obtenerEstados() {
        List<EstadoEntity> lista = repo.findAll();
        return lista.stream()
                .map(entity -> {
                    EstadoDTO dto = new EstadoDTO();
                    dto.setId(entity.getId());
                    dto.setNombreEstado(entity.getNombreEstado());
                    return dto;
                }).collect(Collectors.toList());
    }


    public EstadoDTO insertarEstado(EstadoDTO dto) {
        EstadoEntity entity = new EstadoEntity();
        entity.setNombreEstado(dto.getNombreEstado()); // No seteamos ID, se genera con secuencia
        EstadoEntity guardado = repo.save(entity);
        return convertirADTO(guardado);
    }

    public EstadoDTO actualizarEstado(Long id, EstadoDTO dto) {
        EstadoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionEstadoNoEncontrado("No existe un estado con ID: " + id));

        existente.setNombreEstado(dto.getNombreEstado());
        EstadoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    public boolean eliminarEstado(Long id) {
        try {
            EstadoEntity existente = repo.findById(id).orElse(null);
            if (existente != null) {
                repo.deleteById(id);
                return true;
            } else {
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            throw new ExceptionEstadoNoEncontrado("No se encontró el estado con ID: " + id + " para eliminar.");
        }
    }

    private EstadoDTO convertirADTO(EstadoEntity entity) {
        EstadoDTO dto = new EstadoDTO();
        dto.setId(entity.getId());
        dto.setNombreEstado(entity.getNombreEstado());
        return dto;
    }
}
