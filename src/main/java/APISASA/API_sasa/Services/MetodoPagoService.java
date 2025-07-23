package APISASA.API_sasa.Services;

import APISASA.API_sasa.Entities.MetodoPagoEntity;
import APISASA.API_sasa.Exceptions.ExceptionMetodoNoEncontrado;
import APISASA.API_sasa.Models.DTO.MetodoPagoDTO;
import APISASA.API_sasa.Repositories.MetodoPagoRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository repo;

    // ✅ Obtener todos los métodos de pago
    public List<MetodoPagoDTO> obtenerMetodosDePago() {
        List<MetodoPagoEntity> lista = repo.findAll();
        return lista.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    // ✅ Registrar un nuevo método
    public MetodoPagoDTO insertarMetodo(MetodoPagoDTO dto) {
        if (dto == null || dto.getMetodo() == null || dto.getMetodo().isBlank()) {
            throw new IllegalArgumentException("El nombre del método no puede estar vacío.");
        }

        // Verifica duplicado si tienes restricción UNIQUE
        if (repo.existsByMetodoIgnoreCase(dto.getMetodo())) {
            throw new IllegalArgumentException("Ya existe un método con el nombre: " + dto.getMetodo());
        }

        try {
            MetodoPagoEntity nuevo = new MetodoPagoEntity();
            nuevo.setMetodo(dto.getMetodo()); // ID lo genera la secuencia en Oracle
            MetodoPagoEntity guardado = repo.save(nuevo);
            return convertirADTO(guardado);
        } catch (Exception e) {
            log.error("Error al registrar método: ", e);
            throw new RuntimeException("No se pudo registrar el método: " + e.getMessage());
        }
    }

    // ✅ Actualizar un método existente
    public MetodoPagoDTO actualizarMetodo(Long id, @Valid MetodoPagoDTO dto) {
        MetodoPagoEntity existente = repo.findById(id)
                .orElseThrow(() -> new ExceptionMetodoNoEncontrado("No se encontró el método con ID: " + id));

        existente.setMetodo(dto.getMetodo());
        MetodoPagoEntity actualizado = repo.save(existente);
        return convertirADTO(actualizado);
    }

    // ✅ Eliminar método por ID
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
            throw new RuntimeException("No se encontró el método con ID: " + id + " para eliminar.");
        }
    }

    // 🔁 Conversor Entity → DTO
    private MetodoPagoDTO convertirADTO(MetodoPagoEntity entity) {
        MetodoPagoDTO dto = new MetodoPagoDTO();
        dto.setId(entity.getId());
        dto.setMetodo(entity.getMetodo());
        return dto;
    }
}
